package net.momirealms.craftengine.fabric.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.charset.StandardCharsets;
import java.util.OptionalInt;

public interface NetworkCodecs {

    NetworkCodec<ByteBuf, Boolean> BOOLEAN = new NetworkCodec<>() {
        @Override
        public Boolean decode(ByteBuf in) {
            return in.readBoolean();
        }

        @Override
        public void encode(ByteBuf out, Boolean value) {
            out.writeBoolean(value);
        }
    };

    NetworkCodec<ByteBuf, Byte> BYTE = new NetworkCodec<>() {
        @Override
        public Byte decode(ByteBuf in) {
            return in.readByte();
        }

        @Override
        public void encode(ByteBuf out, Byte value) {
            out.writeByte(value);
        }
    };

    NetworkCodec<ByteBuf, Short> SHORT = new NetworkCodec<>() {
        @Override
        public Short decode(ByteBuf in) {
            return in.readShort();
        }

        @Override
        public void encode(ByteBuf out, Short value) {
            out.writeShort(value);
        }
    };

    NetworkCodec<ByteBuf, Integer> UNSIGNED_SHORT = new NetworkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            return in.readUnsignedShort();
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            out.writeShort(value);
        }
    };

    NetworkCodec<ByteBuf, Integer> INTEGER = new NetworkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            return in.readInt();
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            out.writeInt(value);
        }
    };

    NetworkCodec<ByteBuf, Integer> VAR_INTEGER = new NetworkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            int result = 0;
            int bytesRead = 0;
            byte currentByte;
            do {
                currentByte = in.readByte();
                result |= (currentByte & 127) << bytesRead++ * 7;
                if (bytesRead > 5) {
                    throw new RuntimeException("VarInt too big");
                }
            } while ((currentByte & 128) == 128);
            return result;
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            while ((value & -128) != 0) {
                out.writeByte(value & 127 | 128);
                value >>>= 7;
            }
            out.writeByte(value);
        }
    };

    NetworkCodec<ByteBuf, OptionalInt> OPTIONAL_VAR_INTEGER = VAR_INTEGER.map(
            integer -> integer == 0 ? OptionalInt.empty() : OptionalInt.of(integer - 1),
            optionalInt -> optionalInt.isPresent() ? optionalInt.getAsInt() + 1 : 0
    );

    NetworkCodec<ByteBuf, Long> LONG = new NetworkCodec<>() {
        @Override
        public Long decode(ByteBuf in) {
            return in.readLong();
        }

        @Override
        public void encode(ByteBuf out, Long value) {
            out.writeLong(value);
        }
    };

    NetworkCodec<ByteBuf, Long> VAR_LONG = new NetworkCodec<>() {
        @Override
        public Long decode(ByteBuf in) {
            long result = 0L;
            int bytesRead = 0;
            byte currentByte;
            do {
                currentByte = in.readByte();
                result |= (long)(currentByte & 127) << bytesRead++ * 7;
                if (bytesRead > 10) {
                    throw new RuntimeException("VarLong too big");
                }
            } while ((currentByte & 128) == 128);
            return result;
        }

        @Override
        public void encode(ByteBuf out, Long value) {
            while ((value & -128L) != 0L) {
                out.writeByte((int)(value & 127L) | 128);
                value >>>= 7;
            }
            out.writeByte(value.intValue());
        }
    };

    NetworkCodec<ByteBuf, Float> FLOAT = new NetworkCodec<>() {
        @Override
        public Float decode(ByteBuf in) {
            return in.readFloat();
        }

        @Override
        public void encode(ByteBuf out, Float value) {
            out.writeFloat(value);
        }
    };

    NetworkCodec<ByteBuf, Double> DOUBLE = new NetworkCodec<>() {
        @Override
        public Double decode(ByteBuf in) {
            return in.readDouble();
        }

        @Override
        public void encode(ByteBuf out, Double value) {
            out.writeDouble(value);
        }
    };

    NetworkCodec<ByteBuf, byte[]> BYTE_ARRAY = new NetworkCodec<>() {
        @Override
        public byte[] decode(ByteBuf in) {
            int maxSize = in.readableBytes();
            int size = VAR_INTEGER.decode(in);
            if (size > maxSize) {
                throw new DecoderException("ByteArray with size " + size + " is bigger than allowed " + maxSize);
            } else {
                byte[] bytes = new byte[size];
                in.readBytes(bytes);
                return bytes;
            }
        }

        @Override
        public void encode(ByteBuf out, byte[] value) {
            VAR_INTEGER.encode(out, value.length);
            out.writeBytes(value);
        }
    };

    NetworkCodec<ByteBuf, long[]> LONG_ARRAY = new NetworkCodec<>() {
        @Override
        public long[] decode(ByteBuf in) {
            int arrayLength = VAR_INTEGER.decode(in);
            int maxPossibleElements = in.readableBytes() / 8;
            if (arrayLength > maxPossibleElements) {
                throw new DecoderException("LongArray with size " + arrayLength + " is bigger than allowed " + maxPossibleElements);
            } else {
                long[] longArray = new long[arrayLength];
                for (int i = 0; i < longArray.length; i++) {
                    longArray[i] = in.readLong();
                }
                return longArray;
            }
        }

        @Override
        public void encode(ByteBuf out, long[] value) {
            VAR_INTEGER.encode(out, value.length);
            for (long element : value) {
                out.writeLong(element);
            }
        }
    };

    NetworkCodec<ByteBuf, String> STRING_UTF8 = new NetworkCodec<>() {
        private static final int MAX_STRING_LENGTH = 32767;

        @Override
        public String decode(ByteBuf in) {
            int maxEncodedBytes = ByteBufUtil.utf8MaxBytes(MAX_STRING_LENGTH);
            int encodedLength = VAR_INTEGER.decode(in);
            if (encodedLength > maxEncodedBytes) {
                throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + encodedLength + " > " + maxEncodedBytes + ")");
            } else if (encodedLength < 0) {
                throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
            } else {
                int availableBytes = in.readableBytes();
                if (encodedLength > availableBytes) {
                    throw new DecoderException("Not enough bytes in buffer, expected " + encodedLength + ", but got " + availableBytes);
                } else {
                    String decodedString = in.toString(in.readerIndex(), encodedLength, StandardCharsets.UTF_8);
                    in.readerIndex(in.readerIndex() + encodedLength);
                    if (decodedString.length() > MAX_STRING_LENGTH) {
                        throw new DecoderException("The received string length is longer than maximum allowed (" + decodedString.length() + " > " + MAX_STRING_LENGTH + ")");
                    } else {
                        return decodedString;
                    }
                }
            }
        }

        @Override
        public void encode(ByteBuf out, String value) {
            if (value.length() > MAX_STRING_LENGTH) {
                throw new EncoderException("String too big (was " + value.length() + " characters, max " + MAX_STRING_LENGTH + ")");
            } else {
                int maxPossibleBytes = ByteBufUtil.utf8MaxBytes(value);
                ByteBuf tempBuffer = out.alloc().buffer(maxPossibleBytes);
                try {
                    int actualEncodedBytes = ByteBufUtil.writeUtf8(tempBuffer, value);
                    int maxAllowedBytes = ByteBufUtil.utf8MaxBytes(MAX_STRING_LENGTH);
                    if (actualEncodedBytes > maxAllowedBytes) {
                        throw new EncoderException("String too big (was " + actualEncodedBytes + " bytes encoded, max " + maxAllowedBytes + ")");
                    }
                    VAR_INTEGER.encode(out, actualEncodedBytes);
                    out.writeBytes(tempBuffer);
                } finally {
                    tempBuffer.release();
                }
            }
        }
    };

    NetworkCodec<ByteBuf, Vector3f> VECTOR3F = new NetworkCodec<>() {
        @Override
        public Vector3f decode(ByteBuf in) {
            return new Vector3f(in.readFloat(), in.readFloat(), in.readFloat());
        }

        @Override
        public void encode(ByteBuf out, Vector3f value) {
            out.writeFloat(value.x());
            out.writeFloat(value.y());
            out.writeFloat(value.z());
        }
    };

    NetworkCodec<ByteBuf, Quaternionf> QUATERNIONF = new NetworkCodec<>() {
        @Override
        public Quaternionf decode(ByteBuf in) {
            return new Quaternionf(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
        }

        @Override
        public void encode(ByteBuf out, Quaternionf value) {
            out.writeFloat(value.x());
            out.writeFloat(value.y());
            out.writeFloat(value.z());
            out.writeFloat(value.w());
        }
    };

    NetworkCodec<ByteBuf, Integer> CONTAINER_ID = VAR_INTEGER;

    NetworkCodec<ByteBuf, Integer> RGB_COLOR = new NetworkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            return 255 << 24 | in.readByte() & 0xFF << 16 | in.readByte() & 0xFF << 8 | in.readByte() & 0xFF;
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            out.writeByte(value >> 16 & 0xFF);
            out.writeByte(value >> 8 & 0xFF);
            out.writeByte(value & 0xFF);
        }
    };
}

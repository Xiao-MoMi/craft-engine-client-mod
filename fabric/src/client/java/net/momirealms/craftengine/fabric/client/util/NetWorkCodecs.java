package net.momirealms.craftengine.fabric.client.util;

import io.netty.buffer.ByteBuf;

public interface NetWorkCodecs {

    NetWorkCodec<Boolean> BOOLEAN = new NetWorkCodec<>() {
        @Override
        public Boolean decode(ByteBuf in) {
            return in.readBoolean();
        }

        @Override
        public void encode(ByteBuf out, Boolean value) {
            out.writeBoolean(value);
        }
    };

    NetWorkCodec<Integer> INTEGER = new NetWorkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            return in.readInt();
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            out.writeInt(value);
        }
    };

}

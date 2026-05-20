package net.momirealms.craftengine.fabric.block;

public class VisualBlockStatesData {
    public final int[] data;
    private volatile boolean isReceived;

    public VisualBlockStatesData(int size) {
        this.data = new int[size];
    }

    public void receiveDataChunk(int startIndex, int[] chunk) {
        if (isReceived) return;
        System.arraycopy(chunk, 0, this.data, startIndex, chunk.length);
    }

    public void setReceived() {
        isReceived = true;
    }

    public boolean isReceived() {
        return isReceived;
    }
}

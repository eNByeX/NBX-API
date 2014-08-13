package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

public class NBSHeader {

    private short ticks = 0;
    private short layers = 0;
    private String name = "";
    private String author = "";
    private String originalAuthor = "";
    private String description = "";
    private short tempo = 1000;
    private boolean autosave = false;
    private byte autosaveTime = 1;
    private byte timeSig = 4;
    private int minutes = 0;
    private int lclicks = 0;
    private int rclicks = 0;
    private int blockAdds = 0;
    private int blockBreaks = 0;
    private String importName = "";

    public static NBSHeader read(INBSReader reader) throws IOException {
        NBSHeader header = new NBSHeader();
        header.setTicks(reader.readShort());
        header.setLayers(reader.readShort());
        header.setName(reader.readASCII());
        header.setAuthor(reader.readASCII());
        header.setOriginalAuthor(reader.readASCII());
        header.setDescription(reader.readASCII());
        header.setTempo(reader.readShort());
        header.setAutosave(reader.readBoolean(), reader.readByte());
        header.setTimeSig(reader.readByte());
        header.setMinutes(reader.readInt());
        header.setLClicks(reader.readInt());
        header.setRClicks(reader.readInt());
        header.setBlockAdds(reader.readInt());
        header.setBlockBreaks(reader.readInt());
        header.setImportName(reader.readASCII());
        return header;
    }

    public short getTicks() {
        return ticks;
    }

    public void setTicks(short ticks) {
        this.ticks = ticks;
    }

    public short getLayers() {
        return layers;
    }

    public void setLayers(short layers) {
        this.layers = layers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public void setOriginalAuthor(String originalAuthor) {
        this.originalAuthor = originalAuthor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getTempo() {
        return tempo;
    }

    public void setTempo(short tempo) {
        this.tempo = tempo;
    }

    public boolean shouldAutosave() {
        return autosave;
    }

    public void setAutosave(boolean autosave, byte autosaveTime) {
        this.autosave = autosave;
        this.autosaveTime = autosaveTime;
    }

    public byte getAutosaveTime() {
        return autosaveTime;
    }

    public void setAutosaveTime(byte autosaveTime) {
        this.autosaveTime = autosaveTime;
    }

    public byte getTimeSig() {
        return timeSig;
    }

    public void setTimeSig(byte timeSig) {
        this.timeSig = timeSig;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getLClicks() {
        return lclicks;
    }

    public void setLClicks(int lclicks) {
        this.lclicks = lclicks;
    }

    public int getRClicks() {
        return rclicks;
    }

    public void setRClicks(int rclicks) {
        this.rclicks = rclicks;
    }

    public int getBlockAdds() {
        return blockAdds;
    }

    public void setBlockAdds(int blockAdds) {
        this.blockAdds = blockAdds;
    }

    public int getBlockBreaks() {
        return blockBreaks;
    }

    public void setBlockBreaks(int blockBreaks) {
        this.blockBreaks = blockBreaks;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public NBSHeader copy() {
        NBSHeader header = new NBSHeader();
        header.setTicks(ticks);
        header.setLayers(layers);
        header.setName(name);
        header.setAuthor(author);
        header.setOriginalAuthor(originalAuthor);
        header.setDescription(description);
        header.setTempo(tempo);
        header.setAutosave(autosave, autosaveTime);
        header.setTimeSig(timeSig);
        header.setMinutes(minutes);
        header.setLClicks(lclicks);
        header.setRClicks(rclicks);
        header.setBlockAdds(blockAdds);
        header.setBlockBreaks(blockBreaks);
        header.setImportName(importName);
        return header;
    }

    public void write(INBSWriter writer) throws IOException {
        writer.writeShort(ticks);
        writer.writeShort(layers);
        writer.writeASCII(name);
        writer.writeASCII(author);
        writer.writeASCII(originalAuthor);
        writer.writeASCII(description);
        writer.writeShort(tempo);
        writer.writeBoolean(autosave);
        writer.write(autosaveTime);
        writer.write(timeSig);
        writer.writeInt(minutes);
        writer.writeInt(lclicks);
        writer.writeInt(rclicks);
        writer.writeInt(blockAdds);
        writer.writeInt(blockBreaks);
        writer.writeASCII(importName);
    }

}

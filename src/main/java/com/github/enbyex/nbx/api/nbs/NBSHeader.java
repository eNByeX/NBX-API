package com.github.enbyex.nbx.api.nbs;

import com.github.enbyex.nbx.api.stream.nbs.INBSReader;
import com.github.enbyex.nbx.api.stream.nbs.INBSWriter;
import com.github.enbyex.nbx.api.helper.INBSData;

import java.io.IOException;

public class NBSHeader implements INBSData {

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

    public NBSHeader() {
    }

    public NBSHeader(NBSHeader header) {
        setTicks(header.getTicks());
        setLayers(header.getLayers());
        setName(header.getName());
        setAuthor(header.getAuthor());
        setOriginalAuthor(header.getOriginalAuthor());
        setDescription(header.getDescription());
        setTempo(header.getTempo());
        setAutosave(header.shouldAutosave(), header.getAutosaveTime());
        setTimeSig(header.getTimeSig());
        setMinutes(header.getMinutes());
        setLClicks(header.getLClicks());
        setRClicks(header.getRClicks());
        setBlockAdds(header.getBlockAdds());
        setBlockBreaks(header.getBlockBreaks());
        setImportName(header.getImportName());
    }

    @Override
    public void read(INBSReader reader) throws IOException {
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
        return new NBSHeader(this);
    }

    @Override
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

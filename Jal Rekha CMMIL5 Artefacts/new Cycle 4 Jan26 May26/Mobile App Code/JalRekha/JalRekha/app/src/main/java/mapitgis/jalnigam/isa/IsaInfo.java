package mapitgis.jalnigam.isa;

public class IsaInfo {
    private final String piuId,piu,schemeId,scheme,blockId,block,gpId,gp,villageId,village;

    public IsaInfo(String piuId, String piu, String schemeId, String scheme, String blockId, String block, String gpId, String gp, String villageId, String village) {
        this.piuId = piuId;
        this.piu = piu;
        this.schemeId = schemeId;
        this.scheme = scheme;
        this.blockId = blockId;
        this.block = block;
        this.gpId = gpId;
        this.gp = gp;
        this.villageId = villageId;
        this.village = village;
    }

    public String getPiuId() {
        return piuId;
    }

    public String getPiu() {
        return piu;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public String getScheme() {
        return scheme;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getBlock() {
        return block;
    }

    public String getGpId() {
        return gpId;
    }

    public String getGp() {
        return gp;
    }

    public String getVillageId() {
        return villageId;
    }

    public String getVillage() {
        return village;
    }
}

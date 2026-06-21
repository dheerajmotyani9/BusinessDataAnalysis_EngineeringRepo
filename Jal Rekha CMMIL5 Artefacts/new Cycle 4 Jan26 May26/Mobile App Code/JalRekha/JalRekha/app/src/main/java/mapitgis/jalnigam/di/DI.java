package mapitgis.jalnigam.di;

public class DI {
    private final long id;
    private final String address,village,gp,block,detail,application,scheme,piu,component,comment,review,images;
    private boolean uploaded;

    public DI(long id,String address, String village, String gp, String block, String detail, String application, String scheme, String piu, String component, String comment, String review, String images,boolean uploaded) {
        this.id=id;
        this.address = address;
        this.village = village;
        this.gp = gp;
        this.block = block;
        this.detail = detail;
        this.application = application;
        this.scheme = scheme;
        this.piu = piu;
        this.component = component;
        this.comment = comment;
        this.review = review;
        this.images = images;
        this.uploaded = uploaded;
    }

    public long getId() {
        return id;
    }


    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public String getImages() {
        return images;
    }

    public String getAddress() {
        return address;
    }

    public String getVillage() {
        return village;
    }

    public String getGp() {
        return gp;
    }

    public String getBlock() {
        return block;
    }

    public String getDetail() {
        return detail;
    }

    public String getApplication() {
        return application;
    }

    public String getScheme() {
        return scheme;
    }

    public String getPiu() {
        return piu;
    }

    public String getComponent() {
        return component;
    }

    public String getComment() {
        return comment;
    }

    public String getReview() {
        return review;
    }
}

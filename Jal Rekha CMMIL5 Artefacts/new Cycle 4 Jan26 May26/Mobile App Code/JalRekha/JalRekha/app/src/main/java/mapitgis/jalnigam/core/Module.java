package mapitgis.jalnigam.core;

public class Module {
    public final String name,code;
    public static final Module ASSETS_MAPPING = new Module("101","Assets Mapping");
    public static final Module ANUSHRAVAN = new Module("102","अनुश्रवण");
    public static final Module DHARA = new Module("103","धारा");
    public static final Module ISA = new Module("104","सहभागिता");
    public static final Module DI = new Module("999","Daily Inspection");
    public static final Module NIRMAL = new Module("105","निर्मल");

    private Module(String code,String name) {
        this.code = code;
        this.name = name;
    }
}

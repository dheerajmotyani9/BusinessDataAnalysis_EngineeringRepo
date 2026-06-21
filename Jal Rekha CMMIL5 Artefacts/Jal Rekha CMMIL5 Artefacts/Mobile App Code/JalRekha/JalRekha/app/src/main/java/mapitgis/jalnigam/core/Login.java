package mapitgis.jalnigam.core;

public class Login {
    private final long id;
    private final int roleId;
    private final String name,email,mobile,district,designation,role,token,module;//,did;

    public Login(long id, String name, String email, String mobile, String district, String designation,int roleId, String role, String token,String module) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.district = district;
        this.designation = designation;
        this.roleId = roleId;
        this.role = role;
        this.token = token;
        this.module = module != null ?module:"";
    }

    public String getModule() {
        return module;
    }

    public long getId() {
        return id;
    }

    public String getIdS() {
        return String.valueOf(id);
    }

    public String getToken() {
        return token;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRole() {
        return role;
    }


    public String getRoleLC() {
        return role.toLowerCase();
    }

    public String getDistrict() {
        return district;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDesignation() {
        return designation;
    }

    public boolean isDailyInspection(){
        return roleId!=10; //excluding dhara contractor
    }
    public boolean isAssets(){
        return module.contains(Module.ASSETS_MAPPING.code);
    }
    public boolean isAnusravana(){ return module.contains(Module.ANUSHRAVAN.code); }
    public boolean isDhara(){
        return module.contains(Module.DHARA.code);
    }
    public boolean isIsa(){
        return module.contains(Module.ISA.code);
    }
    public boolean isDI(){
        return module.contains(Module.DI.code);
    }
    public boolean isNirmal(){ return module.contains(Module.NIRMAL.code); }
}

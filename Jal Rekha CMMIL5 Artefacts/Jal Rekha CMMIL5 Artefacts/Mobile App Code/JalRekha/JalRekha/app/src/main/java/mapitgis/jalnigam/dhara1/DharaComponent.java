package mapitgis.jalnigam.dhara1;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Locale;

import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Utility;

public class DharaComponent implements Serializable {
    private final int type, id, schemeId, wtpId, esrId, bid, meterId, meterStatusId;
    private final double r1, r2, ws;
    private final String name, scheme, esr,esrSUID, meter, beneficiary, meterStatusName;
    private final String date, fDate, r1Date;
    private String remark, photo;
    private final double lat, lng;
    private final boolean allFilled;
    private final boolean nextPreviousFilled;

    public DharaComponent(int type, int id, String name, int schemeId, String scheme, int wtpId, int esrId, String esrSUID,String esr, int bid, String beneficiary, int meterId, String meter, double r1, double r2, double ws, String remark, String photo, String r1Date, String date, double lat, double lng, int meterStatusId, String meterStatusName, boolean allFilled, boolean nextPreviousFilled) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.schemeId = schemeId;
        this.scheme = scheme;
        this.wtpId = wtpId;
        this.esrId = esrId;
        this.esrSUID = esrSUID;
        this.esr = esr;
        this.bid = bid;
        this.beneficiary = beneficiary;
        this.meterId = meterId;
        this.meter = meter;
        this.r1 = r1;
        this.r2 = r2;
        this.ws = ws;
        this.remark = remark;
        this.photo = photo;
        this.r1Date = r1Date;
        this.date = date;
        this.fDate = Utility.getFormatedDate(date);
        this.lat = lat;
        this.lng = lng;
        this.meterStatusId = meterStatusId;
        this.meterStatusName = meterStatusName;
        this.allFilled = allFilled;
        this.nextPreviousFilled = nextPreviousFilled;
    }

    public double getWs() {
        return ws;
    }

    public int getMeterStatusId() {
        return meterStatusId;
    }

    public String getMeterStatusName() {
        return meterStatusName;
    }

    public boolean isNextPreviousFilled() {
        return nextPreviousFilled;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }


    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public boolean isAllFilled() {
        return allFilled;
    }

    public int getMeterId() {
        return meterId;
    }

    public String getMeter() {
        return meter;
    }

    public String getR1Date() {
        return r1Date;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public String getPhoto() {
        return photo;
    }

    public String getRemark() {
        return remark;
    }

    public double getR1() {
        return r1;
    }

    public int getBid() {
        return bid;
    }

    public double getR2New() {
        return r2;
    }


    public boolean isFilled() {
        return (r2 > 0 || ws > 0);
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSchemeId() {
        return schemeId;
    }

    public String getScheme() {
        return scheme;
    }

    public int getWtpId() {
        return wtpId;
    }

    public int getEsrId() {
        return esrId;
    }

    public String getEsr() {
        return esr;
    }

    public String getEsrSUID() {
        return esrSUID;
    }

    public int getColor(Context context) {
        return ContextCompat.getColor(context, type == 1 ? R.color.c1 : (type == 2 ? R.color.c2 : type == 3 ? R.color.c3 : R.color.c4));
    }

    public String getR1Reading() {
        return Utility.reading(r1);
    }

    public String getR2Reading() {
        return Utility.reading(r2);
    }

    public String getWSReading() {
        return Utility.reading(ws);
    }

    public String getR1DateDetail() {
        return String.format("Start Reading is End Reading of %s", r1Date);
    }

    public String getFDate() {
        return fDate;
    }

    public int getMAIN_ID() {
        return type == 1 ? schemeId : type == 2 ? wtpId : type == 3 ? esrId : type == 4 ? bid : 0;
    }

    public String getRemarkWithNA() {
        return remark.isEmpty() ? "N/A" : remark;
    }

    public String getFullPhotoURL() {
        return String.format("%s%s", BuildConfig.JAL_NIGAM_IMAGE, photo);
    }


//    public String getTotal() {
//        double ttl = r2 - r1;
//        return String.format(Locale.UK, "%.1f", ttl > 0 ? ttl : 0);
//    }
}

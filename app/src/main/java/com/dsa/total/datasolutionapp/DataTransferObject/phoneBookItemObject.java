package com.dsa.total.datasolutionapp.DataTransferObject;

/**
 * Created by jisun on 2017-05-19.
 */

public class phoneBookItemObject {

    //주소록 아이디
    private int _id;

    //주소록 이름
    private String telName;

    //주소록 번호
    private String telNumber;

    //주소록 주소
    private String telAddress;

    //데이터 출처 헬퍼
    private String telFromDataHelper;

    /**
     * 생성자
     * @param telName
     * @param telNumber
     * @param telAddress
     * @param dataHelper
     */
    public phoneBookItemObject(String telName, String telNumber, String telAddress, String dataHelper) {
        this.telName = telName;
        this.telNumber = telNumber;
        this.telAddress = telAddress;
        this.telName = telName;
    }

    public String getTelName() {
        return telName;
    }

    public void setTelName(String telName) {
        this.telName = telName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getTelAddress() {
        return telAddress;
    }

    public void setTelAddress(String telAddress) {
        this.telAddress = telAddress;
    }

    public String getTelFromDataHelper() {
        return telFromDataHelper;
    }

    public void setTelFromDataHelper(String telFromDataHelper) {
        this.telFromDataHelper = telFromDataHelper;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}

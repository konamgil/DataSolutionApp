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
     * 모든 필드를 초기화하는 생성자이다
     * @param _id
     * @param telName
     * @param telNumber
     * @param telAddress
     * @param telFromDataHelper
     */
    public phoneBookItemObject(int _id, String telName, String telAddress, String telNumber,  String telFromDataHelper) {
        this._id = _id;
        this.telName = telName;
        this.telAddress = telAddress;
        this.telNumber = telNumber;
        this.telFromDataHelper = telFromDataHelper;
    }

    public phoneBookItemObject() {
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

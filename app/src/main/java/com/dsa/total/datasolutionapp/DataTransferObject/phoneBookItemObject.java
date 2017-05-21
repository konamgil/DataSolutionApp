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
    public phoneBookItemObject(int _id, String telName, String telNumber, String telAddress, String telFromDataHelper) {
        this._id = _id;
        this.telName = telName;
        this.telNumber = telNumber;
        this.telAddress = telAddress;
        this.telFromDataHelper = telFromDataHelper;
    }

    /**
     * id 없이 정보만 가져와서 생성자를 초기화 한다
     * 생성자
     * @param telName
     * @param telNumber
     * @param telAddress
     * @param telFromDataHelper
     */
    public phoneBookItemObject(String telName, String telNumber, String telAddress, String telFromDataHelper) {
        this.telName = telName;
        this.telNumber = telNumber;
        this.telAddress = telAddress;
        this.telFromDataHelper = telFromDataHelper;
    }

    /**
     * 현재 json 파일로부터 세개의 필드만 받아와서 초기화하고있다(안쓰고있음)
     * @param telName
     * @param telNumber
     * @param telAddress
     */
    public phoneBookItemObject(String telName, String telNumber, String telAddress) {
        this.telName = telName;
        this.telNumber = telNumber;
        this.telAddress = telAddress;
        this.telName = telName;
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

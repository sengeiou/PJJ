package com.pjj.xsp.module.bean;


/**
 * Created by Administrator on 2017/11/15.
 */
public class BoardBean {


    /**
     * Ret : 1
     * Data : {"Floor":"1","DoorState":"FF","UpDown":"FF","FaultReportA":"FF","FaultReportB":"FF","ManCheck":"12"}
     */

    private int Ret;
    private DataBean Data;

    public int getRet() {
        return Ret;
    }

    public void setRet(int Ret) {
        this.Ret = Ret;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * Floor : 1
         * DoorState : FF
         * UpDown : FF
         * FaultReportA : FF
         * FaultReportB : FF
         * ManCheck : 12
         */

        private String Floor;
        private String DoorState;
        private String UpDown;
        private String FaultReportA;
        private String FaultReportB;
        private String register;
        //11 有人, 12 无人
        private String ManCheck;

        public String getFloor() {
            return Floor;
        }

        public void setFloor(String Floor) {
            this.Floor = Floor;
        }

        public String getDoorState() {
            return DoorState;
        }

        public void setDoorState(String DoorState) {
            this.DoorState = DoorState;
        }

        public String getUpDown() {
            return UpDown;
        }

        public void setUpDown(String UpDown) {
            this.UpDown = UpDown;
        }

        public String getFaultReportA() {
            return FaultReportA;
        }

        public void setFaultReportA(String FaultReportA) {
            this.FaultReportA = FaultReportA;
        }

        public String getFaultReportB() {
            return FaultReportB;
        }

        public void setFaultReportB(String FaultReportB) {
            this.FaultReportB = FaultReportB;
        }

        public String getRegister() {
            return register;
        }

        public void setRegister(String register) {
            this.register = register;
        }

        public String getManCheck() {
            return ManCheck;
        }

        public void setManCheck(String manCheck) {
            ManCheck = manCheck;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "Floor='" + Floor + '\'' +
                    ", DoorState='" + DoorState + '\'' +
                    ", UpDown='" + UpDown + '\'' +
                    ", FaultReportA='" + FaultReportA + '\'' +
                    ", FaultReportB='" + FaultReportB + '\'' +
                    ", register='" + register + '\'' +
                    ", ManCheck='" + ManCheck + '\'' +
                    '}';
        }
    }

    public boolean isChange(BoardBean boardBean) {
        if (boardBean == null) {
            return true;
        }
        if (boardBean.getRet() != Ret) {
            return true;
        }
        DataBean data = boardBean.getData();
        if (data == Data) {
            return false;
        } else {
            if (null != Data && null != data) {
                return !equals(data.getUpDown(), Data.getUpDown()) ||
                        !equals(data.getDoorState(), Data.getDoorState()) ||
                        !equals(data.getFaultReportA(), Data.getFaultReportA()) ||
                        !equals(data.getFaultReportB(), Data.getFaultReportB()) ||
                        !equals(data.getFloor(), Data.getFloor());
            } else {
                return true;
            }
        }
    }

    private boolean equals(String s, String s1) {
        if (null == s && s1 == null) {
            return true;
        }
        return null != s && s.equals(s1);
    }
}

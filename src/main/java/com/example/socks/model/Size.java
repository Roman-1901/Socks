package com.example.socks.model;

public enum Size {
    S(23), M(25), L(27), XL(29), XXL(31), XXXL(33);
    private Integer sizeNum;

    Size(Integer sizeNum) {
        this.sizeNum = sizeNum;
    }

    public Integer getSizeNum() {
        return sizeNum;
    }

    public void setSizeNum(Integer sizeNum) {
        this.sizeNum = sizeNum;
    }
}


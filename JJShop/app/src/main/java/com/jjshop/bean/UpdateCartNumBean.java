package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/8/6
 */

public class UpdateCartNumBean extends BaseBean{
    private UpdateCartNumData data;

    public UpdateCartNumData getData() {
        return data;
    }

    public void setData(UpdateCartNumData data) {
        this.data = data;
    }

    public class UpdateCartNumData{
        private int stock;

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }
}

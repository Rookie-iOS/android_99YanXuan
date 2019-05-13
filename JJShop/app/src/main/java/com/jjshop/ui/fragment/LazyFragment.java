package com.jjshop.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

    public abstract class LazyFragment extends Fragment {

        protected View mFragment;
        /**
         * 是否对用户可见的标志位
         */
        private boolean isVisible;
        /**
         * 判断view是不是已经填充完毕的标记位
         */
        private boolean isPrepared;
        /**
         * 是否已经加载过数据
         */
        private boolean isAlreadyLoadData = false;

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {

            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                isVisible = true;
                onVisible();
            } else {
                isVisible = false;
                onInVisible();
            }
        }

        /**
         * setUserVisibleHint为true时调用的方法
         */
        private void onVisible() {
            lazyLode();
        }

        /**
         * setUserVisibleHint为false时调用的方法
         */
        private void onInVisible() {
            if (isAlreadyLoadData) {
                InVisibleEvent();
                System.out.println("LazyFragment.onInVisible:InVisibleEvent");
            }
        }

        /**
         * 多封装一层，把initLazyLodeData中需要做的判断封装起来
         */
        private void lazyLode() {
            //确保View初始化完成
            if (!isVisible||!isPrepared) {
                return;
            }
            //加载数据
            if (!isAlreadyLoadData) {//如果没有加载过数据
                initLazyLodeData();
                isAlreadyLoadData = true;
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (mFragment == null) {
                mFragment = View.inflate(getActivity(), getLayoutId(), null);
            }
            ViewGroup parent = (ViewGroup) mFragment.getParent();
            if (parent != null) {
                parent.removeView(mFragment);
            }

            initview();
            isPrepared = true;
            System.out.println("LazyFragment.onCreateView"+"----->"+getClassName());
            return mFragment;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            lazyLode();
            initData();
            initEvent();
        }

        /**
         * 布局xml文件的id
         * @return
         */
        public abstract int getLayoutId();

        /**
         * 在onCreateView中调用，可以执行findViewbyId操作
         */
        public abstract void initview();

        /**
         * 初始化懒加载的数据
         */
        public abstract void initLazyLodeData();

        /**
         * 初始化数据
         */
        public abstract void initData();

        /**
         * 初始化事件
         */
        public abstract void initEvent();

        /**
         * 加载过数据后，fragment变为不可见之后的需要执行的操作
         */
        public abstract void InVisibleEvent();

        public String getClassName() {
            return getClass().getSimpleName();
        }

        @Override
        public void onResume() {
            super.onResume();
            System.out.println("onResume"+"----->"+getClassName());
        }

        @Override
        public void onPause() {
            super.onPause();
            System.out.println("onPause"+"----->"+getClassName());
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            System.out.println("onDestroyView"+"----->"+getClassName());
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            System.out.println("onDestroy"+"----->"+getClassName());
        }
}

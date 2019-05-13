package com.jjshop.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

/**
 * 作者：张国庆
 * 时间：2018/11/10
 */

public class HttpCNS implements Dns{
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        JjLog.e("HttpDns", "lookup:" + hostname);
//        String ip = DNSHelper.getIpByHost(hostname);
//        if (ip != null && !ip.equals("")) {
//            List<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(ip));
//            JjLog.e("HttpDns", "inetAddresses:" + inetAddresses);
//            return inetAddresses;
//        }
        return Dns.SYSTEM.lookup(hostname);
    }
}

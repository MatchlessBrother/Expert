package com.expert.cleanup.nets.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AscEncryptHelper
{
    public static final String secretKey = "WASE@#TGE23456uhtnp3454zXvkfgopedg-0p-[0;oli5yuwranzx";

    public static String encrypt(String content, String secretKey)
    {
        return encryptOrDecrypt(content, secretKey, "E");
    }

    public static String decrypt(String content, String secretKey)
    {
        return content == null || content.isEmpty() ? "" : encryptOrDecrypt(content, secretKey, "D");
    }

    private static String encryptOrDecrypt(String content, String secretKey, String operation)
    {
        secretKey = md5(secretKey);//32位 md5值
        int secretKeyLength = secretKey.length();
        content = (null != operation && operation.trim().equals("D") ? DefineBase64.decode(content) : md5(content + secretKey).subSequence(0, 16) + content);
        int[] box = new int[256];
        int[] rndkey = new int[256];
        //0-31循环取 secretKey asc2码 box 赋值0-255
        char[] secretKeyBytes = secretKey.toCharArray();
        for (int i = 0; i < 256; i++)
        {
            rndkey[i] = secretKeyBytes[i % secretKeyLength];
            box[i] = i;
        }
        /*
         * j-> 0-255 之间取值
         *
         * j1 = (0 + 0 + asc[0]) % 256
         * j2 = (j1 + 1 + asc[1]) % 256
         * j3 = (j2 + 2 + asc[2]) % 256
         */
        for (int i = 0, j = 0; i < 256; i++)
        {
            j = (j + box[i] + rndkey[i]) % 256;
            int tmp = box[i];
            box[i] = box[j];
            box[j] = tmp;
        }
        /*
         * a,j -> 0-255
         */
        char[] contentBytes = content.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int a = 0, i = 0, j = 0; i < contentBytes.length; i++)
        {
            a = (a + 1) % 256;
            j = (j + box[a]) % 256;
            int tmp = box[a];
            box[a] = box[j];
            box[j] = tmp;
            result.append((char) (contentBytes[i] ^ (box[(box[a] + box[j]) % 256])));
        }

        if (null != operation && operation.equals("D"))
        {
            if (result.substring(0, 16).equals(md5(result.substring(16) + secretKey).substring(0, 16)))
            {
                return result.substring(16);
            }
            else
            {
                return "";
            }
        }
        else
        {
            return DefineBase64.encode(result.toString());
        }
    }

    private static String md5(String string)
    {
        if (string == null || string.trim().isEmpty())
        {
            return "";
        }
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes)
            {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1)
                {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
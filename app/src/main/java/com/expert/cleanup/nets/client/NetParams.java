package com.expert.cleanup.nets.client;

import com.expert.cleanup.nets.util.AscEncryptHelper;
import java.io.File;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NetParams
{
    public synchronized static NetParams getInstance()
    {
        if(null == mNetParams)
            mNetParams = new NetParams();
        mNetParams.clearAllData();
        return mNetParams;
    }

    private static NetParams mNetParams;

    private NetParams()
    {

    }

    /*********************************网络请求参数/本地查询参数************************************/
    private List<String> mFilesPath = new LinkedList<String>();
    private List<String> mImagesPath = new LinkedList<String>();
    private Map<String, String> mForms = new ConcurrentHashMap<String, String>();
    private Map<String, String> mAllowSameKeyForms = new IdentityHashMap<String, String>();
    private List<MultipartBody.Part> mMultipartFiles = new LinkedList<MultipartBody.Part>();
    private List<MultipartBody.Part> mMultipartImages = new LinkedList<MultipartBody.Part>();
    private Map<String, RequestBody> mMultipartForms = new ConcurrentHashMap<String, RequestBody>();
    private Map<String, RequestBody> mMultipartAllowSameKeyForms = new IdentityHashMap<String, RequestBody>();

    /*****************************网络请求参数类型/本地查询参数类型********************************/
    private MediaType mTextType = MediaType.parse("text/plain");
    private MediaType mImageType = MediaType.parse("multipart/form-data");
    private MediaType mFileType = MediaType.parse("application/otcet-stream");

    /**********************************************************************************************/
    /**************************************增加本地参数操作****************************************/
    /**********************************************************************************************/
    public NetParams putFilePath(String filePath)
    {
        if(null != filePath && !"".equals(filePath.trim()))
            mFilesPath.add(filePath);
        return this;
    }

    public NetParams putFilesPath(List<String> filesPath)
    {
        if(null != filesPath && filesPath.size() > 0)
        {
            for(String filePath : filesPath)
            {
                putFilePath(filePath);
            }
        }
        return this;
    }

    public NetParams putImagePath(String imagePath)
    {
        if(null != imagePath && !"".equals(imagePath.trim()))
            mImagesPath.add(imagePath);
        return this;
    }

    public NetParams putImagesPath(List<String> imagesPath)
    {
        if(null != imagesPath && imagesPath.size() > 0)
        {
            for(String imagePath : imagesPath)
            {
                putImagePath(imagePath);
            }
        }
        return this;
    }

    public NetParams putForm(String key, String value)
    {
        if(null != key && null != value)
            mForms.put(key,value);
        return this;
    }

    public NetParams putForms(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
               putForm(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    public NetParams putFormAddEncry(String key, String value)
    {
        if(null != key && null != value)
            mForms.put(key,AscEncryptHelper.encrypt(value, AscEncryptHelper.secretKey));
        return this;
    }

    public NetParams putFormsAddEncry(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
                putForm(entry.getKey(),AscEncryptHelper.encrypt(entry.getValue(),AscEncryptHelper.secretKey));
            }
        }
        return this;
    }

    public NetParams putAllowSameKeyForm(String key, String value)
    {
        if(null != key && null != value)
            mAllowSameKeyForms.put(key,value);
        return this;
    }

    public NetParams putAllowSameKeyForms(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
                putAllowSameKeyForm(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    public NetParams putAllowSameKeyFormAddEncry(String key, String value)
    {
        if(null != key && null != value)
            mAllowSameKeyForms.put(key,AscEncryptHelper.encrypt(value,AscEncryptHelper.secretKey));
        return this;
    }

    public NetParams putAllowSameKeyFormsAddEncry(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
                putAllowSameKeyForm(entry.getKey(),AscEncryptHelper.encrypt(entry.getValue(),AscEncryptHelper.secretKey));
            }
        }
        return this;
    }


    /**********************************************************************************************/
    /**************************************删除本地参数操作****************************************/
    /**********************************************************************************************/
    public NetParams removeFilePath(String filePath)
    {
        if(null != filePath && !"".equals(filePath.trim()))
        {
            for(String mFilePath : mFilesPath)
            {
                if(mFilePath.toLowerCase().trim().equals(filePath.toLowerCase().trim()))
                {
                    mFilesPath.remove(mFilePath);
                }
            }
        }
        return this;
    }

    public NetParams removeFilesPath(List<String> filesPath)
    {
        if(null != filesPath && filesPath.size() > 0)
        {
            for(String filePath : filesPath)
            {
                removeFilePath(filePath);
            }
        }
        return this;
    }

    public NetParams removeImagePath(String imagePath)
    {
        if(null != imagePath && !"".equals(imagePath.trim()))
        {
            for(String mImagePath : mImagesPath)
            {
                if(mImagePath.toLowerCase().trim().equals(imagePath.toLowerCase().trim()))
                {
                    mImagesPath.remove(mImagePath);
                }
            }
        }
        return this;
    }

    public NetParams removeImagesPath(List<String> imagesPath)
    {
        if(null != imagesPath && imagesPath.size() > 0)
        {
            for(String imagePath : imagesPath)
            {
                removeImagePath(imagePath);
            }
        }
        return this;
    }

    public NetParams removeForm(String key)
    {
        if(null != key && mForms.containsKey(key))
            mForms.remove(key);
        return this;
    }

    public NetParams removeForms(List<String> keys)
    {
        if(null != keys && keys.size() > 0)
        {
            for(String key : keys)
            {
                removeForm(key);
            }
        }
        return this;
    }

    public NetParams removeForms(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
                removeForm(entry.getKey());
            }
        }
        return this;
    }

    public NetParams removeAllowSameKeyForm(String key)
    {
        if(null != key && mAllowSameKeyForms.containsKey(key))
            mAllowSameKeyForms.remove(key);
        return this;
    }

    public NetParams removeAllowSameKeyForms(List<String> keys)
    {
        if(null != keys && keys.size() > 0)
        {
            for(String key : keys)
            {
                removeAllowSameKeyForm(key);
            }
        }
        return this;
    }

    public NetParams removeAllowSameKeyForms(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
                removeAllowSameKeyForm(entry.getKey());
            }
        }
        return this;
    }


    /**********************************************************************************************/
    /**************************************更改本地参数操作****************************************/
    /**********************************************************************************************/
    public NetParams replaceForm(String key, String value)
    {
        if(null != key && null != value)
        {
            removeForm(key);
            putForm(key,value);
        }
        return this;
    }

    public NetParams replaceForms(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
                replaceForm(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    public NetParams replaceAllowSameKeyForm(String key, String value)
    {
        if(null != key && null != value)
        {
            removeAllowSameKeyForm(key);
            putAllowSameKeyForm(key,value);
        }
        return this;
    }

    public NetParams replaceAllowSameKeyForms(Map<String, String> forms)
    {
        if(null != forms && forms.size() > 0)
        {
            for(Map.Entry<String, String> entry : forms.entrySet())
            {
                replaceAllowSameKeyForm(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }


    /**********************************************************************************************/
    /*************************************转换本地参数为网络参数***********************************/
    /**********************************************************************************************/
    public NetParams convertFiles()
    {
        mMultipartFiles.clear();
        if(null != mFilesPath && mFilesPath.size() > 0)
        {
            for(int index = 0;index < mFilesPath.size();index++)
            {
                String filePath = mFilesPath.get(index);
                File file = new File(filePath);
                RequestBody requestFile = RequestBody.create(mFileType,file);
                //MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file" + index , file.getName(), requestFile);
                MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                mMultipartFiles.add(multipartFilePart);
            }
        }
        return this;
    }

    public NetParams convertImages()
    {
        mMultipartImages.clear();
        if(null != mImagesPath && mImagesPath.size() > 0)
        {
            for(int index = 0;index < mImagesPath.size();index++)
            {
                String imagePath = mImagesPath.get(index);
                File image = new File(imagePath);
                RequestBody requestImage = RequestBody.create(mImageType,image);
                MultipartBody.Part multipartImagePart = MultipartBody.Part.createFormData("image" + index , image.getName(), requestImage);
                mMultipartImages.add(multipartImagePart);
            }
        }
        return this;
    }

    public NetParams convertForms()
    {
        mMultipartForms.clear();
        if(null != mForms && mForms.size() > 0)
        {
            for(Map.Entry<String, String> entry : mForms.entrySet())
            {
                RequestBody requestBody = RequestBody.create(mTextType,entry.getValue());
                mMultipartForms.put(entry.getKey(),requestBody);
            }
        }
        return this;
    }

    public NetParams convertAllowSameKeyForms()
    {
        mMultipartAllowSameKeyForms.clear();
        if(null != mAllowSameKeyForms && mAllowSameKeyForms.size() > 0)
        {
            for(Map.Entry<String, String> entry : mAllowSameKeyForms.entrySet())
            {
                RequestBody requestBody = RequestBody.create(mTextType,entry.getValue());
                mMultipartAllowSameKeyForms.put(entry.getKey(),requestBody);
            }
        }
        return this;
    }


    /**********************************************************************************************/
    /****************************************获取参数数据******************************************/
    /**********************************************************************************************/
    public void clearAllData()
    {
        if(null != mForms)mForms.clear();
        if(null != mFilesPath)mFilesPath.clear();
        if(null != mImagesPath)mImagesPath.clear();
        if(null != mMultipartFiles)mMultipartFiles.clear();
        if(null != mMultipartForms)mMultipartForms.clear();
        if(null != mMultipartImages)mMultipartImages.clear();
        if(null != mAllowSameKeyForms)mAllowSameKeyForms.clear();
        if(null != mMultipartAllowSameKeyForms)mMultipartAllowSameKeyForms.clear();
    }

    public List<String> getFilesPath()
    {
        return mFilesPath;

    }

    public List<String> getImagesPath()
    {
        return mImagesPath;

    }

    public Map<String, String> getForms()
    {
        return mForms;

    }

    public Map<String, String> getAllowSameKeyForms()
    {
        return mAllowSameKeyForms;

    }

    public List<MultipartBody.Part> getMultipartFiles()
    {
        return mMultipartFiles;

    }

    public List<MultipartBody.Part> getMultipartImages()
    {
        return mMultipartImages;

    }

    public Map<String, RequestBody> getMultipartForms()
    {
        return mMultipartForms;

    }

    public Map<String, RequestBody> getMultipartAllowSameKeyForms()
    {
        return mMultipartAllowSameKeyForms;

    }
}
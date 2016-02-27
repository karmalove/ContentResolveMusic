package com.example.kevin.contentresolvemusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2016/2/27.
 */
public class MusicLoader {
    private static final String TAG = "MusicLoader";
    //定义一个音乐数据数组
    private List<MusicInfo> musicList = new ArrayList<MusicInfo>();
    private static ContentResolver contentResolver;
    private static MusicLoader musicLoader;
    //获得音乐uri
    private Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };
    private String where =  "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 " ;
    private String sortOrder = MediaStore.Audio.Media.DATA;

    public static MusicLoader instance(ContentResolver pcontentResolver) {
        if (musicLoader == null) {
            contentResolver = pcontentResolver;
            musicLoader = new MusicLoader();
        }
        return musicLoader;
    }

    private MusicLoader()

    {
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {

        } else if (!cursor.moveToFirst()) {

        } else
            try {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                    int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setTitle(title);
                    musicInfo.setUrl(url);
                    musicInfo.setArtist(artist);
                    musicInfo.setAlbum(album);
                    musicInfo.setId(id);
                    musicInfo.setSize(size);
                    musicInfo.setDuration(duration);

                    musicList.add(musicInfo);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
    }
    public List<MusicInfo> getMusicList(){
        return musicList;
    }

    //内部类MusicInfo接口Parcelable对象分解序列化
    static class MusicInfo implements Parcelable {
        private long id;
        private int duration;//音乐时长
        private long size;
        private String artist;
        private String url;
        private String title;
        private String album;//唱片集

        public MusicInfo() {
        }

        public MusicInfo(long id, String title) {
            this.id = id;
            this.title = title;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);//写入id
            dest.writeInt(duration);
            dest.writeLong(size);
            dest.writeString(artist);
            dest.writeString(url);
            dest.writeString(title);
            dest.writeString(album);
        }
        public static final Parcelable.Creator<MusicInfo> CREATOR= new Creator<MusicInfo>() {
            @Override
            public MusicInfo createFromParcel(Parcel source) {
                MusicInfo musicInfo=new MusicInfo();
                musicInfo.setArtist(source.readString());
                musicInfo.setTitle(source.readString());
                musicInfo.setDuration(source.readInt());
                musicInfo.setSize(source.readLong());
                musicInfo.setId(source.readLong());//读取id
                musicInfo.setAlbum(source.readString());
                musicInfo.setUrl(source.readString());
                return musicInfo;
            }

            @Override
            public MusicInfo[] newArray(int size) {
                return new MusicInfo[size];
            }
        };
    }
}


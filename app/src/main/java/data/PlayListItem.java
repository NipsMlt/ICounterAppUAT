package data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayListItem {

@SerializedName("id")
@Expose
private String id;
@SerializedName("title")
@Expose
private String title;
@SerializedName("album")
@Expose
private String album;
@SerializedName("albumID")
@Expose
private String albumID;
@SerializedName("artist")
@Expose
private String artist;
@SerializedName("artistID")
@Expose
private String artistID;
@SerializedName("track")
@Expose
private Integer track;
@SerializedName("year")
@Expose
private String year;
@SerializedName("duration")
@Expose
private String duration;
@SerializedName("coverArt")
@Expose
private String coverArt;
@SerializedName("ArtistArt")
@Expose
private Integer artistArt;
@SerializedName("allowoffline")
@Expose
private Integer allowoffline;
@SerializedName("genre")
@Expose
private String genre;
@SerializedName("AlbumArt")
@Expose
private String albumArt;
@SerializedName("keywords")
@Expose
private List<String> keywords = null;
@SerializedName("verified")
@Expose
private Integer verified;
@SerializedName("cleardetails")
@Expose
private Integer cleardetails;
@SerializedName("bitrate")
@Expose
private Integer bitrate;
@SerializedName("size")
@Expose
private String size;
@SerializedName("explicit")
@Expose
private String explicit;
@SerializedName("single")
@Expose
private String single;
@SerializedName("extras")
@Expose
private String extras;
@SerializedName("likes")
@Expose
private String likes;
@SerializedName("videoid")
@Expose
private String videoid;
@SerializedName("thumbnailid")
@Expose
private String thumbnailid;
@SerializedName("videoreleasedate")
@Expose
private String videoreleasedate;
@SerializedName("nouservideo")
@Expose
private String nouservideo;
@SerializedName("isexclusivesong")
@Expose
private String isexclusivesong;
@SerializedName("albumexclusive")
@Expose
private Integer albumexclusive;
@SerializedName("displaytype")
@Expose
private String displaytype;
@SerializedName("bigimages")
@Expose
private String bigimages;
@SerializedName("name")
@Expose
private String name;
@SerializedName("deeplink")
@Expose
private String deeplink;
@SerializedName("selected")
@Expose
private String selected;
@SerializedName("coverartid")
@Expose
private String coverartid;
@SerializedName("songorder")
@Expose
private Integer songorder;
@SerializedName("coverArtID")
@Expose
private String coverArtID;
@SerializedName("coverArtImage")
@Expose
private String coverArtImage;
@SerializedName("details")
@Expose
private String details;
@SerializedName("FeaturedDesc")
@Expose
private String featuredDesc;
@SerializedName("hasvideo")
@Expose
private String hasvideo;
@SerializedName("coverArtSize")
@Expose
private Integer coverArtSize;
@SerializedName("PlaylistCount")
@Expose
private String playlistCount;
@SerializedName("count")
@Expose
private String count;
@SerializedName("cachedtime")
@Expose
private String cachedtime;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
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

public String getAlbumID() {
return albumID;
}

public void setAlbumID(String albumID) {
this.albumID = albumID;
}

public String getArtist() {
return artist;
}

public void setArtist(String artist) {
this.artist = artist;
}

public String getArtistID() {
return artistID;
}

public void setArtistID(String artistID) {
this.artistID = artistID;
}

public Integer getTrack() {
return track;
}

public void setTrack(Integer track) {
this.track = track;
}

public String getYear() {
return year;
}

public void setYear(String year) {
this.year = year;
}

public String getDuration() {
return duration;
}

public void setDuration(String duration) {
this.duration = duration;
}

public String getCoverArt() {
return coverArt;
}

public void setCoverArt(String coverArt) {
this.coverArt = coverArt;
}

public Integer getArtistArt() {
return artistArt;
}

public void setArtistArt(Integer artistArt) {
this.artistArt = artistArt;
}

public Integer getAllowoffline() {
return allowoffline;
}

public void setAllowoffline(Integer allowoffline) {
this.allowoffline = allowoffline;
}

public String getGenre() {
return genre;
}

public void setGenre(String genre) {
this.genre = genre;
}

public String getAlbumArt() {
return albumArt;
}

public void setAlbumArt(String albumArt) {
this.albumArt = albumArt;
}

public List<String> getKeywords() {
return keywords;
}

public void setKeywords(List<String> keywords) {
this.keywords = keywords;
}

public Integer getVerified() {
return verified;
}

public void setVerified(Integer verified) {
this.verified = verified;
}

public Integer getCleardetails() {
return cleardetails;
}

public void setCleardetails(Integer cleardetails) {
this.cleardetails = cleardetails;
}

public Integer getBitrate() {
return bitrate;
}

public void setBitrate(Integer bitrate) {
this.bitrate = bitrate;
}

public String getSize() {
return size;
}

public void setSize(String size) {
this.size = size;
}

public String getExplicit() {
return explicit;
}

public void setExplicit(String explicit) {
this.explicit = explicit;
}

public String getSingle() {
return single;
}

public void setSingle(String single) {
this.single = single;
}

public String getExtras() {
return extras;
}

public void setExtras(String extras) {
this.extras = extras;
}

public String getLikes() {
return likes;
}

public void setLikes(String likes) {
this.likes = likes;
}

public String getVideoid() {
return videoid;
}

public void setVideoid(String videoid) {
this.videoid = videoid;
}

public String getThumbnailid() {
return thumbnailid;
}

public void setThumbnailid(String thumbnailid) {
this.thumbnailid = thumbnailid;
}

public String getVideoreleasedate() {
return videoreleasedate;
}

public void setVideoreleasedate(String videoreleasedate) {
this.videoreleasedate = videoreleasedate;
}

public String getNouservideo() {
return nouservideo;
}

public void setNouservideo(String nouservideo) {
this.nouservideo = nouservideo;
}

public String getIsexclusivesong() {
return isexclusivesong;
}

public void setIsexclusivesong(String isexclusivesong) {
this.isexclusivesong = isexclusivesong;
}

public Integer getAlbumexclusive() {
return albumexclusive;
}

public void setAlbumexclusive(Integer albumexclusive) {
this.albumexclusive = albumexclusive;
}

public String getDisplaytype() {
return displaytype;
}

public void setDisplaytype(String displaytype) {
this.displaytype = displaytype;
}

public String getBigimages() {
return bigimages;
}

public void setBigimages(String bigimages) {
this.bigimages = bigimages;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getDeeplink() {
return deeplink;
}

public void setDeeplink(String deeplink) {
this.deeplink = deeplink;
}

public String getSelected() {
return selected;
}

public void setSelected(String selected) {
this.selected = selected;
}

public String getCoverartid() {
return coverartid;
}

public void setCoverartid(String coverartid) {
this.coverartid = coverartid;
}

public Integer getSongorder() {
return songorder;
}

public void setSongorder(Integer songorder) {
this.songorder = songorder;
}

public String getCoverArtID() {
return coverArtID;
}

public void setCoverArtID(String coverArtID) {
this.coverArtID = coverArtID;
}

public String getCoverArtImage() {
return coverArtImage;
}

public void setCoverArtImage(String coverArtImage) {
this.coverArtImage = coverArtImage;
}

public String getDetails() {
return details;
}

public void setDetails(String details) {
this.details = details;
}

public String getFeaturedDesc() {
return featuredDesc;
}

public void setFeaturedDesc(String featuredDesc) {
this.featuredDesc = featuredDesc;
}

public String getHasvideo() {
return hasvideo;
}

public void setHasvideo(String hasvideo) {
this.hasvideo = hasvideo;
}

public Integer getCoverArtSize() {
return coverArtSize;
}

public void setCoverArtSize(Integer coverArtSize) {
this.coverArtSize = coverArtSize;
}

public String getPlaylistCount() {
return playlistCount;
}

public void setPlaylistCount(String playlistCount) {
this.playlistCount = playlistCount;
}

public String getCount() {
return count;
}

public void setCount(String count) {
this.count = count;
}

public String getCachedtime() {
return cachedtime;
}

public void setCachedtime(String cachedtime) {
this.cachedtime = cachedtime;
}

}
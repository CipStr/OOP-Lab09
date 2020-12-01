package it.unibo.oop.lab.lambda.ex02;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.*;

/**
 *
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new HashMap<>();
    private final Set<Song> songs = new HashSet<>();

    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }

    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }

    @Override
    public Stream<String> orderedSongNames() {
    	final List<String> list = new ArrayList<>();
    	songs.forEach(l -> {
    		list.add(l.songName);
    	});
    	return list.stream().sorted((i, j) -> i.compareTo(j));
    }

    @Override
    public Stream<String> albumNames() {
    	final List<String> albList = new ArrayList<>();
    	albums.forEach((s,i) -> {
    		albList.add(s);
    	});
    	return albList.stream();
    }

    @Override
    public Stream<String> albumInYear(final int year) {
    	final List<String> list = new ArrayList<>();
    	albums.forEach((k,v) -> {
    		if(v == year) {
    			list.add(k);
    		}
    	});
    	return list.stream();
    }

    @Override
    public int countSongs(final String albumName) {
    	int cont;
    	cont = (int) songs.stream().filter(l -> l.getAlbumName().equals(Optional.of(albumName))).count();
    	return cont; 
    }

    @Override
    public int countSongsInNoAlbum() {
    	int cont;
    	cont = (int) songs.stream().filter(l -> l.getAlbumName().isEmpty()).count();
    	return cont; 
    }

    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
    	final int cont;
    	cont=this.countSongs(albumName);
        final  double[] durArr = new double[1];
        songs.stream().filter(l -> l.getAlbumName().equals(Optional.of(albumName))).forEach(l -> {
        	durArr[0] = durArr[0] + l.getDuration();
        });
        final double temp = durArr[0];
        return OptionalDouble.of(temp / cont);
    }

    @Override
    public Optional<String> longestSong() {
    	final double[] durArr = new double[1];
    	final String[] songName = new String[1];
    	songs.stream().forEach(l -> {
    		if (durArr[0] < l.getDuration()) {
    			durArr[0] = l.getDuration();
    			songName[0] = l.getSongName();
    		}
        	});
    	return Optional.of(songName[0]);
    } 

    @Override
    public Optional<String> longestAlbum() {
    	final String[] songName = new String[1];
    	final String temp = this.longestSong().get();
    	songs.stream().filter(l -> l.getSongName().equals(temp)).forEach(l -> {
    		final Optional<String> alb;
    		alb = l.getAlbumName();
    		songName[0] = alb.get();
    	});
    	return Optional.of(songName[0]);
    }

    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;

        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }

        public String getSongName() {
            return songName;
        }

        public Optional<String> getAlbumName() {
            return albumName;
        }

        public double getDuration() {
            return duration;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = songName.hashCode() ^ albumName.hashCode() ^ Double.hashCode(duration);
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Song) {
                final Song other = (Song) obj;
                return albumName.equals(other.albumName) && songName.equals(other.songName)
                        && duration == other.duration;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}

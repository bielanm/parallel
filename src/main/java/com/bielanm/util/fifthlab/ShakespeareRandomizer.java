package com.bielanm.util.fifthlab;

import com.bielanm.util.Randomizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ShakespeareRandomizer {

    private static final Randomizer rnd = new Randomizer();
    List<Sonet> sonets;

    public ShakespeareRandomizer(List<Sonet> sonets) {
        this.sonets = sonets;
    }

    public Sonet getRandom() {
        return sonets.get(rnd.nextInt(sonets.size()));
    }
    public static ShakespeareRandomizer newInstance(String path) throws IOException {
        String filestr = Files.newBufferedReader(Paths.get(path))
                        .lines()
                        .collect(Collectors.joining());
        List<Sonet> sonets = Arrays.asList(filestr.split("NEXT")).stream()
        .map(str -> new Sonet(Arrays.asList(str.split("\\$")))).collect(Collectors.toList());
        return new ShakespeareRandomizer(sonets);
    }

    public static class Sonet extends ArrayList<String> {
        public Sonet(Collection<? extends String> c) {
            super(c);
        }
    }
}

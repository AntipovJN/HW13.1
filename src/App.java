import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) throws IOException {
        Animal[] animalM1 = {new Animal("Cat"), new Animal("Dog"), new Animal("Elephant"),
                new Animal("Cock"), new Animal("Bull"), new Animal("Ant"),
                new Animal("Tentecles"), new Animal("Worm")};
        ByteArrayOutputStream bai = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bai);
        oos.writeInt(animalM1.length);
        for (int i = 0; i < animalM1.length; i++) {
            oos.writeObject(animalM1[i]);
        }
        oos.flush();
        oos.close();
        for(Animal animal : deserializeAnimalArray(bai.toByteArray())){
            System.out.println(animal.toString());
        }
            
        Comparator comparator = ( a,  b) -> ((a.equals(b) ? 0: (a.hashCode() <  b.hashCode()) ? -1 : 1));
        Stream stream = Stream.of("cat","dog has biggest hash","Y?","It's impossible");
        BiConsumer minMaxConsumer = (s1, s2) -> System.out.println( s1 + " " + s2);
        findMinMax(stream, comparator, minMaxConsumer);
    }

    public static <T> void findMinMax(
            Stream<? extends T> stream,
            Comparator<? super T> order,
            BiConsumer<? super T, ? super T> minMaxConsumer) {
        LinkedList<T> list = new LinkedList<>(stream.sorted(order).collect(Collectors.toList()));
        if (!list.isEmpty()) {
              minMaxConsumer.accept(list.getFirst(), list.getLast());
        } else {
            minMaxConsumer.accept(null, null);
        }
    }

     public static Animal[] deserializeAnimalArray(byte[] data) {
        ByteArrayInputStream inputByteArrayStream = new ByteArrayInputStream(data);
        Animal[] array;
        try {
            ObjectInputStream objectIO = new ObjectInputStream(inputByteArrayStream);
            int count = objectIO.readInt();
            array = new Animal[count];
            for (int i = 0; i < array.length; i++) {
                array[i] = (Animal) objectIO.readObject();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        return array;
    }
}

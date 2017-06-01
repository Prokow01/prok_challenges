
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author peterrokowski
 */
public class permuteGen {

    char[] seed;
    int size;
    ArrayList<Character> chars = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();

    public permuteGen(String s) {
        this.seed = s.toCharArray();
        size = seed.length;
        Arrays.sort(seed);
        list.add(String.copyValueOf(seed));

        chars.add(seed[0]);

        for (int i = 0; i < seed.length; i++) {
            if (chars.indexOf(seed[i]) < 0) {
                chars.add(seed[i]);
            }
        }
        //System.out.println(chars.indexOf(seed[2]));
    }

    public void build() {
        int index = -1;
        for (int i = 0; i < chars.size(); i++) {
            index++;
            int end = size - 1;

            while (index != end) {

                char tmp = seed[end];
                seed[end] = seed[index];
                seed[index] = tmp;

                String t = String.copyValueOf(seed);

                if (list.indexOf(t) < 0) {
                    list.add(t);
                } else if (list.indexOf(t) >= 0) {
                    end--;
                }
                System.out.println(t);
            }

        }
        Collections.sort(list);
    }

    public void printList() {
        System.out.println(list);
        System.out.println(chars);
        System.out.println(list.size());
    }

}

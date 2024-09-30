import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;

public class ShamirSecretSharing {
    public static void main(String[] args) throws Exception {
        String testCase1 = readFile("testcase1.json");
        String testCase2 = readFile("testcase2.json");

        processTestCase(testCase1);
        processTestCase(testCase2);
    }

    private static void processTestCase(String jsonData) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonData);
        int n = jsonObject.getJSONObject("keys").getInt("n");
        int k = jsonObject.getJSONObject("keys").getInt("k");

        List<int[]> points = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            if (jsonObject.has(String.valueOf(i))) {
                int x = i;
                String base = jsonObject.getJSONObject(String.valueOf(i)).getString("base");
                String value = jsonObject.getJSONObject(String.valueOf(i)).getString("value");
                int y = new BigInteger(value, Integer.parseInt(base)).intValue();
                points.add(new int[]{x, y});
            }
        }

        int secret = findConstantTerm(points, k);
        System.out.println("Secret: " + secret);

        if (n == 9) {
            findWrongPoints(points, secret);
        }
    }

    private static int findConstantTerm(List<int[]> points, int k) {
        int constant = 0;
        for (int i = 0; i < k; i++) {
            int xi = points.get(i)[0];
            int yi = points.get(i)[1];
            int term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = points.get(j)[0];
                    term *= -xj / (xi - xj);
                }
            }
            constant += term;
        }
        return constant;
    }

    private static void findWrongPoints(List<int[]> points, int constant) {
        System.out.println("Wrong points in testcase 2:");
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            int calculatedY = constant;
            if (y != calculatedY) {
                System.out.println("(" + x + ", " + y + ")");
            }
        }
    }

    private static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}

package Model;

import com.badlogic.gdx.files.FileHandle;

public class Csv {
    private String[][] data;

    public Csv(FileHandle file) {
        String text = file.readString();
        String[] rows = text.split("\n");
        int nRows = rows.length;
        int nCols = rows[0].split(",").length;
        data = new String[nRows][nCols];

        for (int i = 0; i < nRows; i++) {
            String[] cells = rows[i].split(",");
            System.arraycopy(cells, 0, data[i], 0, nCols);
        }
    }

    public String[][] getData() { return data; }
}

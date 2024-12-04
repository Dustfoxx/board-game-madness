package Model;

import com.badlogic.gdx.files.FileHandle;

public class Csv {
    String[][] data;

    public Csv(FileHandle file) {
        String text = file.readString();
        int nRows = text.split("\n").length;
        int nCols = text.split(",").length;
        data = new String[nRows][nCols];
        String[] rows = text.split("\n");
        for (int i = 0; i < nRows; i++) {
            String[] cells = rows[i].split(",");
            System.arraycopy(cells, 0, data[i], 0, nCols);
        }

    }
}

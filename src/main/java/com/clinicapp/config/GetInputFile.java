package com.clinicapp.config;

import java.io.*;

public class GetInputFile {
    public InputStream getFile(final String fname) {
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fname);

        if (ioStream == null) {
            throw new IllegalArgumentException(fname + " is not found");
        }
        return ioStream;
    }
    private void printFileContent(InputStream is) throws IOException
    {
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr))
        {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            is.close();
        }
    }
}

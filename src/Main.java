import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws IOException {
        String filepath = "C:\\Users\\Artur\\IdeaProjects\\Visual Cryptography\\izaq5gtxds001.jpg";
        BufferedImage image = loadImage(filepath);
        BufferedImage blackWhite = toBlackAndWhite(image);
        Color [][] matrix = toMatrix(blackWhite);


        int row = blackWhite.getHeight();
        int column = blackWhite.getWidth();

        Color [][] share1 = new Color[row*2][column*2];
        Color [][] share2 = new Color[row*2][column*2];

        ArrayList<Color[][]> comb = combinations();

        Random r = new Random(42);

        for(int i=0;i<row;i++)
        {
            for (int j=0;j<column;j++)
            {

                Color pixelColor = matrix[i][j];
                int pom = r.nextInt(comb.size());

                var replace = comb.get(pom);

                if(isBlack(pixelColor))
                {
                    embedImage(share1,replace,i,j);
                    embedImage(share2,reverseArray(replace),i,j);
                }
                else{
                    embedImage(share1,replace,i,j);
                    embedImage(share2,replace,i,j);
                }
            }
        }

        BufferedImage image1 = ColorMatrixToBitmap(share1);
        BufferedImage image2 = ColorMatrixToBitmap(share2);

        File output1 =new File ("output1.png");
        ImageIO.write(image1,"png",output1);
        File output2 =new File ("output2.png");
        ImageIO.write(image2,"png",output2);




    }


    public static BufferedImage toBlackAndWhite(BufferedImage bf) throws IOException {
        BufferedImage result = new BufferedImage(bf.getWidth(), bf.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(bf, 0, 0, Color.WHITE,null);
        g2d.dispose();




        File output =new File ("converted.bmp");
        ImageIO.write(result,"bmp",output);
        return result;
    }

    public static BufferedImage loadImage(String input) {
        try {
            File file = new File(input);
            BufferedImage in = ImageIO.read(file);
            BufferedImage newImage = new BufferedImage(
                    in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

            return newImage;
        } catch (IOException e) {
            System.err.println("Could not read image with give file path.");
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Color[][]> combinations(){
        ArrayList<Color[][]> list = new ArrayList<Color[][]>(6);

        Color [][] color = new Color[2][2];

        color[0][0]=Color.WHITE;
        color[0][1]=Color.WHITE;
        color[1][0]=Color.BLACK;
        color[1][1]=Color.BLACK;

        list.add(color);
        list.add(reverseArray(color));

        color = new Color[2][2];
        color[0][0] = Color.WHITE; // W B
        color[1][0] = Color.WHITE; // W B
        color[0][1] = Color.BLACK;
        color[1][1] = Color.BLACK;

        list.add(color);
        list.add(reverseArray(color));

        color = new Color[2][2];

        color[0][0] = Color.WHITE; // W B
        color[1][1] = Color.WHITE; // B W
        color[1][0] = Color.BLACK;
        color[0][1] = Color.BLACK;

        list.add(color);
        list.add(reverseArray(color));

        return list;

    }


    public static boolean isBlack(Color color)
    {
        return color.getRed()<10 && color.getBlue()<10 && color.getGreen()<10;
    }

    public static Color reverseSingle(Color color)
    {
        if(isBlack(color))
        {
            return Color.WHITE;
        }
        else
        {
            return Color.BLACK;
        }
    }

    public static Color[][] reverseArray(Color[][] color)
    {
        var rows = color.length;
        var columns = color[0].length;

        var newColor = new Color[rows][columns];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                newColor[i][j] = reverseSingle(color[i][j]);
            }
        }

        return newColor;
    }



    public static Color[][] embedImage( Color[][] target, Color[][] image, int x, int y)
    {
        int imageWidth = image[0].length;
        int imageHeight = image.length;

        int targetWidth = target[0].length;
        int targetHeight = target.length;

        var yStart = imageWidth * y;
        var iStart = imageHeight * x;

        for (int i = iStart, i_image = 0; i < iStart + imageHeight && i < targetHeight && i_image < imageHeight; i++, i_image++)
        {
            for(int j = yStart, j_image = 0; y < yStart + imageWidth && j < targetWidth && j_image < imageWidth; j++, j_image++)
            {
                target[i][j] = image[i_image][j_image];
            }
        }

        return target;
    }


    public static BufferedImage ColorMatrixToBitmap(Color[][] share)
    {
        int height = share.length;
        int width = share[0].length;

        BufferedImage bf = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_BINARY);

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                bf.setRGB(j, i, share[i][j].getRGB());
            }
        }

        return bf;
    }

    public static Color [][] toMatrix(BufferedImage bf)
    {
        Color [][] result = new Color[bf.getHeight()][bf.getWidth()];
        for(int i=0;i<bf.getHeight();i++)
        {
            for(int j=0;j<bf.getWidth();j++)
            {
                int rgb = bf.getRGB(j,i);
                Color temp = new Color(rgb,true);
                result[i][j]=temp;
            }
        }
        return result;
    }

}

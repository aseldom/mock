package ru.checkdev.auth.service;

import ru.checkdev.auth.domain.Photo;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hincu Andrei (andreih1981@gmail.com)on 14.09.2018.
 * @version $Id$.
 * @since 0.1.
 */
public class PhotoMainClass {

    public static void compress(Photo photo) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {
            ByteArrayInputStream bis = new ByteArrayInputStream(photo.getPhoto());
            BufferedImage bufferedImage = ImageIO.read(bis);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.05f);
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
            byte[] bytes = bos.toByteArray();
            writer.dispose();
            photo.setPhoto(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void skip(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/job4j_auth", "postgres", "password");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM photos");
        ResultSet rs = statement.executeQuery();
        List<Photo> photos = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            byte[] bytes = rs.getBytes("photo");
            Photo photo = new Photo();
            photo.setId(id);
            photo.setPhoto(bytes);
            photos.add(photo);
        }
        statement.close();
        System.out.println("---------------------------------------------");

        PreparedStatement ps = connection.prepareStatement("UPDATE photos  SET photo = (?) WHERE id= (?)");
        for (Photo photo : photos) {
            compress(photo);
            ps.setBytes(1, photo.getPhoto());
            ps.setInt(2, photo.getId());
            ps.execute();
        }
        ps.executeBatch();

    }
}

package ru.checkdev.auth.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.checkdev.auth.domain.Notify;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.domain.Photo;
import ru.checkdev.auth.domain.Role;
import ru.checkdev.auth.repository.PersonRepository;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * @author parsentev
 * @since 25.09.2016
 */
@Service
public class PersonService {
    private final Logger log = LoggerFactory.getLogger(PersonService.class);
    private final PasswordEncoder encoding = new BCryptPasswordEncoder();
    private final PersonRepository persons;
    private final Messenger msg;

    @Autowired
    public PersonService(final PersonRepository persons, final Messenger msg) {
        this.persons = persons;
        this.msg = msg;
    }

    public Optional<Profile> reg(Profile profile) {
        Optional<Profile> result = Optional.empty();
        try {
            if (profile.isPrivacy()) {
                profile.setRoles(null);
                profile.setActive(true);
                profile.setKey(
                        this.encoding.encode(
                                String.format("%s%s", System.currentTimeMillis(), profile.getPassword())
                        )
                );
                profile.setPassword(this.encoding.encode(profile.getPassword()));
                profile.setUpdated(Calendar.getInstance());
                result = Optional.of(this.persons.save(profile));
                Map<String, Object> keys = new HashMap<>();
                keys.put("key", profile.getKey());
                this.msg.send(new Notify(profile.getEmail(), keys, Notify.Type.REG.name()));
            }
        } catch (DataIntegrityViolationException e) {
            log.error("not unique email {}", profile.getEmail());
        }
        return result;
    }

    public Optional<Profile> create(Profile profile) {
        Optional<Profile> result = Optional.empty();
        try {
            profile.setPrivacy(true);
            profile.setRoles(null);
            profile.setKey(
                    this.encoding.encode(
                            String.format("%s%s", System.currentTimeMillis(), profile.getPassword())
                    )
            );
            profile.setPassword(this.encoding.encode(profile.getPassword()));
            result = Optional.of(this.persons.save(profile));
        } catch (DataIntegrityViolationException e) {
            log.error("not unique email {}", profile.getEmail());
        }
        return result;
    }

    public Optional<Profile> findByEmail(String email) {
        final Optional<Profile> result;
        Profile profile = this.persons.findByEmail(email);
        if (profile == null) {
            result = Optional.empty();
        } else {
            result = Optional.of(profile);
        }
        return result;
    }

    public List<Profile> getAll() {
        return Lists.newArrayList(persons.findAll());
    }

    public List<Profile> getIn(List<String> keys) {
        return this.persons.findByKeyIn(keys);
    }

    @Transactional
    public boolean activated(String key) {
        Profile profile = this.persons.findByKey(key);
        boolean result = false;
        if (profile != null && !profile.isActive()) {
            profile.setActive(true);
            this.persons.save(profile);
            result = true;
        }
        return result;
    }

    public Optional<Profile> forgot(Profile profile) {
        final Optional<Profile> result;
        Profile find = this.persons.findByEmail(profile.getEmail());
        if (find == null) {
            result = Optional.empty();
        } else {
            String password = RandomStringUtils.randomAlphabetic(8);
            find.setPassword(this.encoding.encode(password));
            this.persons.save(find);
            Map<String, Object> keys = new HashMap<>();
            keys.put("password", password);
            this.msg.send(new Notify(profile.getEmail(), keys, Notify.Type.FORGOT.name()));
            result = Optional.of(profile);
        }
        return result;
    }

    public List<Profile> findAll(Pageable pageable) {
        return this.persons.findAll(pageable).getContent();
    }

    public Long total() {
        return this.persons.total();
    }

    public Profile findById(int id) {
        return this.persons.findById(id).get();
    }

    public void save(Profile profile) {
        this.persons.save(profile);
    }

    @Transactional
    public void saveRole(Profile profile) {
        Profile load = this.persons.findById(profile.getId()).get();
        List<Role> roles = new ArrayList<>();
        for (Role role : profile.getRoles()) {
            if (role != null) {
                roles.add(role);
            }
        }
        if (profile.isActive()) {
            load.setActive(true);
        }
        load.setRoles(roles);
        load.setUpdated(Calendar.getInstance());
        this.persons.save(load);
    }

    public Profile findByKey(String key) {
        return this.persons.findByKey(key);
    }

    public List<Profile> findBySearch(String search, PageRequest email) {
        return this.persons.findByEmailContainingOrUsernameContaining(search, search, email).getContent();
    }

    public List<Profile> findByShow(boolean show, int limit) {
        return this.persons.findByShow(show, PageRequest.of(0, limit));
    }

    public List<Profile> findByShow(boolean show) {
        return this.persons.findByShow(show);
    }

    public List<Profile> findByShow(boolean show, Pageable pageable) {
        return this.persons.findByShow(show, pageable);
    }

    public Long showed() {
        return this.persons.showed();
    }

    public Photo compress(MultipartFile multipartFile) {
        Photo photo = new Photo();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
            byte[] bytes = bos.toByteArray();
            writer.dispose();
            photo.setPhoto(bytes);
            photo.setName(multipartFile.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo;
    }


    public Profile loadFromHh(String linc) {
        Profile profile = null;
        try {
            Document doc = Jsoup.connect(linc).validateTLSCertificates(false).get();
            String name = doc.getElementsByAttributeValue("data-qa", "resume-personal-name").first().text();
            String address = doc.getElementsByAttributeValue("data-qa", "resume-personal-address").first().text();
            String experience = doc.getElementsByClass("resume-block__title-text resume-block__title-text_sub").first().text();
            String salary = doc.getElementsByAttributeValue("data-qa", "resume-block-salary").first().text();
            String aboutShort = doc.getElementsByAttributeValue("data-qa", "resume-block-title-position").first().text();
            StringBuilder about = new StringBuilder();
            String startP = "<p>";
            String endP = "</p>";
            Elements elements = doc.getElementsByAttributeValue("itemprop", "worksFor");
            for (Element element : elements) {
                String text = element.getElementsByClass("bloko-column bloko-column_s-2 bloko-column_m-2 bloko-column_l-2").first().text();
                String companyName = element.getElementsByClass("resume-block__sub-title").first().text();
                String description = element.getElementsByAttributeValue("data-qa", "resume-block-experience-description").first().text();
                about.append(startP).append(text).append(endP);
                about.append(startP).append(companyName).append(endP);
                about.append(startP).append(description).append(endP);
            }
            profile = new Profile(name, experience, salary, aboutShort, about.toString(), address);
            Element img = doc.getElementsByClass("resume-photo__image HH-Bloko-PopupSwitcher-Switcher").first();
            if (img != null) {
                String url = img.absUrl("src");
                profile.setUrlHh(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profile;
    }


    public String update(String email, MultipartFile file, Profile profile) {
        String result = "ok";
        Profile profileDb = persons.findPerson(email);
        Photo photo;
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            if (file.getSize() < 1000000) {
                photo = compress(file);
                if (profileDb.getPhoto() == null) {
                    profileDb.setPhoto(photo);
                } else {
                    BeanUtils.copyProperties(photo, profileDb.getPhoto(), "id");
                }
            } else {
                result = "Photo is very big!";
            }
        } else if (!StringUtils.isEmpty(profile.getUrlHh())) {
            try {
                byte[] bytes = Jsoup.connect(profile.getUrlHh()).validateTLSCertificates(false).ignoreContentType(true).execute().bodyAsBytes();
                String uuidFile = UUID.randomUUID().toString();
                photo = new Photo(bytes, uuidFile);
                if (profileDb.getPhoto() == null) {
                    profileDb.setPhoto(photo);
                } else {
                    BeanUtils.copyProperties(photo, profileDb.getPhoto(), "id");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Не верная ссылка";
            }
        }
        Set<String> nullPropertyNames = getNullPropertyNames(profile, "id", "email", "key", "roles", "privacy", "photo");
        BeanUtils.copyProperties(profile, profileDb, nullPropertyNames.toArray(new String[0]));
        profileDb.setUpdated(Calendar.getInstance());
        profileDb.setActive(true);
        persons.save(profileDb);
        return result;
    }

    private Set<String> getNullPropertyNames(Object source, String... extra) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        emptyNames.addAll(Arrays.asList(extra));
        return emptyNames;
    }
}

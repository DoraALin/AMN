package me.lin.amn.repository.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.lin.amn.common.logging.TracingConfig;
import me.lin.amn.repository.ServerConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by Lin on 6/23/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServerConfig.class, TracingConfig.class} )
public class ArtifactControllerTest {
    /**
     * unit test for artifact uplaod servlet ArtifactController
     * @throws Exception
     */

    @Test
    public void testArtifactUpload() throws Exception {
        Path path = Paths.get("/Users/Lin/Downloads/ClientConfiguration.java");
        byte[] data = Files.readAllBytes(path);
        String boundary = "q1w2e3r4t5y6u7i8o9";
        data = createFileContent(data, boundary, "text/plaint; charset=UTF-8", "ClientConfiguration.java");
        byte[] dataLast = createLastFileContent(data, boundary, "text/plaint; charset=UTF-8", "ClientTest.java");

        ArtifactController artifactController = new ArtifactController();
        MvcResult result = standaloneSetup(artifactController).build()
                .perform(post("/artifacts/new")
                        //.contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Content-Type",
                                "multipart/form-data; boundary=" + boundary)
                        .content(ArrayUtils.addAll(data, dataLast))
                        )
                .andReturn();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<String>>(){}.getType();
        List<String> artifactIds = gson.fromJson(result.getResponse().getContentAsString(), collectionType);
        assertEquals("Artifact uploaded records does not match.", 2, artifactIds.size());
    }

    public byte[] createFileContent(byte[] data, String boundary, String contentType, String fileName){
        String start = "--" + boundary + "\r\n Content-Disposition: form-data; name=\"file\"; filename=\""+fileName+"\"\r\n"
                + "Content-Transfer-Encoding: binary" + "\r\n"
                + "Content-Type: "+contentType+"\r\n\r\n";
        String end = "\r\n";
        return ArrayUtils.addAll(start.getBytes(), ArrayUtils.addAll(data,end.getBytes()));
    }
    public byte[] createLastFileContent(byte[] data, String boundary, String contentType, String fileName){
        String start = "--" + boundary + "\r\n Content-Disposition: form-data; name=\"file\"; filename=\""+fileName+"\"\r\n"
                + "Content-Transfer-Encoding: binary" + "\r\n"
                + "Content-Type: "+contentType+"\r\n\r\n";
        String end = "--" + boundary + "--";
        return ArrayUtils.addAll(start.getBytes(), ArrayUtils.addAll(data,end.getBytes()));
    }
}

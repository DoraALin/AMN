package me.lin.amn.repository.controller;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Lin on 6/20/16.
 */
@RestController
@RequestMapping("/artifacts")
public class ArtifactController {

    private static Log LOG = LogFactory.getLog(ArtifactController.class.getName());

    @Autowired
    ServletFileUpload fileUplaod;

    @RequestMapping(value = "/new", method = POST, headers = "content-type=application/octet-stream")
    public String upload(WebRequest req,
                       NativeWebRequest naReq,
                       RedirectAttributes redirectAttributes){
        HttpServletRequest httpReq = naReq.getNativeRequest(HttpServletRequest.class);
        FileItemIterator iter = null;
        try {
            iter = fileUplaod.getItemIterator(httpReq);
        } catch (FileUploadException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }

        try {
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String fileName = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    //this is a form key/value
                } else {
                    if(LOG.isDebugEnabled()) LOG.debug("File transfer start for : " + fileName);
                    // Process the input stream
                    //TODO: perform artifact upload by ArtifactUpload runnable. Maybe in a latch?
                }
            }
        } catch (FileUploadException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

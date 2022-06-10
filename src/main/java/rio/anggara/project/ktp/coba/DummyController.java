/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package rio.anggara.project.ktp.coba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author rioan
 */
@Controller
public class DummyController {
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    public String getDummy (Model model) {
    int record = dummyController.getDummyCount();
        String result = "";
        try{
            data = dummyController.findDummyEntities().subList(0, record);
        }
        catch (Exception e){
            result=e.getMessage();
        }
        
        model.addAttribute("godummy", data);
        model.addAttribute("record", record);
         
        return "dummy";    
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    
    @PostMapping(value="/newdata", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newDummy(HttpServletRequest data,HttpServletResponse httpResponse, @RequestParam("gambar") MultipartFile file) throws ParseException, Exception{
        Dummy dumdata = new Dummy();
        
        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);
        
        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        byte[] image = file.getBytes();
        
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        dummyController.create(dumdata);
        
        httpResponse.sendRedirect("read");
        return null;
        //return "dummy/create";
        //return "/dummy.html";
    }
    
    @RequestMapping (value="/gambar" , method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
	Dummy dum = dummyController.findDummy(id);
	byte[] gambar = dum.getGambar();
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(gambar);
    }
    
//    @GetMapping("detail/{id}")
//    public String detailDummy(@PathVariable("id")int id, Model m)
//    {
//        List<Dummy> dumdata = dummyController.findDummyEntities(id, id);
//        m.addAttribute("dumdata", dumdata);
//        return "edit";
//    }

    @RequestMapping("/edit/{id}")
    public String editDummy(@PathVariable("id") int id, Model m) throws Exception {
        Dummy dumdata = dummyController.findDummy(id);
        m.addAttribute("godummy", dumdata);
        return "dummy/edit";
    }
    
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateDummy(@RequestParam("gambar")MultipartFile file, HttpServletResponse httpResponse, HttpServletRequest request) throws ParseException, Exception {
        Dummy dumdata = new Dummy();
        
        String id = request.getParameter("id");
        int iid = Integer.parseInt(id);
        
        String tanggal = request.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        byte[] image = file.getBytes();
        
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        
        dummyController.edit(dumdata);
        return "redirect:/read";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteDummy(@PathVariable("id") Integer id) throws Exception {
        dummyController.destroy(id);
        
        return "redirect:/read";
    }
}
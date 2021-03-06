package recoder.single.bangle.remarket.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import recoder.single.bangle.member.DTO.MemberDTO;
import recoder.single.bangle.remarket.DTO.MarketReplyDTO;
import recoder.single.bangle.remarket.service.MarketReplyService;
import utils.XSSprotect;

@Controller
@RequestMapping("/marketReply")
public class MarketReplyController {
	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MarketReplyService service;

	@RequestMapping("/insert.do")
	@ResponseBody
	public String insert() { //댓글작성
		try {
			String recontent = request.getParameter("recontent");
			recontent = XSSprotect.replaceParameter(recontent);
			MemberDTO dto = (MemberDTO) session.getAttribute("loginInfo");
			String writer = dto.getId();
			System.out.println("writer : " + writer);
			int seq = Integer.parseInt(request.getParameter("seq"));
			int boardSeq = seq;
			service.insert(recontent, writer, boardSeq);
			List<MarketReplyDTO> list = service.list(boardSeq);
			Gson g = new Gson();
			String json = g.toJson(list);
			System.out.println(json);
			return json;
		}catch(Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}


	@RequestMapping("/delete.do")
	@ResponseBody
	public String delete() {
		try {
			int seq = Integer.parseInt(request.getParameter("seq"));
			int boardSeq = Integer.parseInt(request.getParameter("boardSeq"));
			System.out.println("삭제할 댓글 시퀀스 : " + seq + " 삭제보드시퀀스 : " + boardSeq);
			service.delete(seq);
			List<MarketReplyDTO> list = service.list(boardSeq);
			Gson g = new Gson();
			String json = g.toJson(list);
			System.out.println(json);
			return json;
		}catch(Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public String update() {
		try {
			int seq = Integer.parseInt(request.getParameter("seq"));
			System.out.println("댓글 수정 도착 : " + seq);
			String recontent = request.getParameter("recontent");
			System.out.println("수정 내용 : " + recontent);
			int result = service.update(recontent, seq);
			if(result > 0) {
				System.out.println("업데이트 성공");
			}
			else {
				System.out.println("업데이트 실패");
			}
			return "{\"recontent\":\""+recontent+"\"}";
		}catch(Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}

	}
}

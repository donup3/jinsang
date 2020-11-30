package com.samplesecurity.controller;

import com.samplesecurity.domain.Reply;
import com.samplesecurity.dto.reply.ReplyFormDto;
import com.samplesecurity.dto.reply.ReplySubFormDto;
import com.samplesecurity.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jinsang/reply")
@Slf4j
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getReplies(@PathVariable("boardId") Long boardId) {

        return new ResponseEntity<>(replyService.getReplyList(boardId), HttpStatus.OK);
    }

    @PostMapping("/write/{boardId}")
    public ResponseEntity<?> addReply(@PathVariable("boardId") Long boardId,
                                      @RequestBody ReplyFormDto replyFormDto,
                                      Authentication authentication) {
        log.info("ReplyFormDto: " + replyFormDto);
        Reply reply = replyFormDto.toEntity();
        replyService.register(boardId, reply, authentication);

        return new ResponseEntity<>(replyService.getReplyList(boardId), HttpStatus.CREATED);
    }

    @PostMapping("/write/subReply/{boardId}")
    public ResponseEntity<?> addSubReply(@PathVariable("boardId") Long boardId,
                                         @RequestBody ReplySubFormDto replySubFormDto,
                                         Authentication authentication) {
        log.info("ReplyFormDto: " + replySubFormDto);
        Reply reply = replySubFormDto.toEntity();
        replyService.subRegister(boardId, reply, authentication, replySubFormDto.getReplyId());

        return new ResponseEntity<>(replyService.getReplyList(boardId), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{boardId}/{replyId}")
    public ResponseEntity<?> remove(@PathVariable("boardId") Long boardId,
                                    @PathVariable("replyId") Long replyId,
                                    Authentication authentication) {
        log.info("delete reply...");
        replyService.delete(replyId, authentication);

        return new ResponseEntity<>(replyService.getReplyList(boardId), HttpStatus.CREATED);
    }
}

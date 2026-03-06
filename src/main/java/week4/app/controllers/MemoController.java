package week4.app.controllers;

import week4.app.models.Memo;
import week4.framework.annotations.*;

import java.util.List;

@RestController
@RequiresAuth
public class MemoController {
    @PostMapping("/api/memo/create")
    public Integer create(@RequestBody Memo memo) {
        return 0;
    }
    @PostMapping("/api/memo/update")
    public Integer update(@RequestBody Memo memo) {
        return 0;
    }
    @PostMapping("/api/memo/delete")
    public void delete(@RequestParam("id") Integer id) {
    }
    @GetMapping("/api/memo/list")
    public List<Memo> list() {
        return null;
    }
}
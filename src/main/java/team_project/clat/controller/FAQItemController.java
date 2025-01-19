package team_project.clat.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team_project.clat.domain.FAQItem;
import team_project.clat.service.FAQItemService;

import java.util.List;

@Tag(name = "faq API", description = "FAQ API")
@RestController
@RequestMapping("/help/faq")
@RequiredArgsConstructor
public class FAQItemController {

  private final FAQItemService faqItemService;

  // GET /help/faq
  @GetMapping
  public ResponseEntity<List<FAQItem>> getFAQItems() {
    return ResponseEntity.ok(faqItemService.getFAQItems());
  }

  @GetMapping("{faqId}")
  public ResponseEntity<FAQItem> getFAQItem(@PathVariable("faqId") Long faqId) {
    return ResponseEntity.ok(faqItemService.getFAQItem(faqId));
  }

  @PutMapping("/modify/{faqId}")
  public ResponseEntity<FAQItem> updateFAQItem(
          @PathVariable("faqId")Long faqId,
          @RequestBody FAQItem faqItem
  ) {
    return ResponseEntity.ok(faqItemService.updateFAQItem(faqId, faqItem));
  }

  @PostMapping
  public ResponseEntity<FAQItem> createFAQItem(@RequestBody FAQItem faqItem) {
    return ResponseEntity.ok(faqItemService.createFAQItem(faqItem));
  }

  @DeleteMapping("{faqId}")
  public ResponseEntity<FAQItem> deleteFAQItem(@PathVariable("faqId") Long faqId) {
    faqItemService.deleteFAQItem(faqId);
    return ResponseEntity.noContent().build();
  }
}

# FRONTIER — Lộ trình mở rộng thành một mod cực lớn

Bản 1.0.0 hiện tại là **nền tảng**: vòng lặp EXPLORE → DISCOVER → COLLECT → FORGE →
CUSTOMIZE → TRAVEL. Mọi giai đoạn dưới đây phải giữ đúng bản sắc Vanilla+:
không biến thành MMO/RPG nặng, không tự động hóa hoàn toàn, VFX tiết chế,
multiplayer chuẩn (server-authoritative), và luôn thân thiện với hiệu năng.

---

## Giai đoạn 2 — "VÙNG BIÊN CHÌM" (thế giới & phe phái)

Mở rộng nửa trên của vòng lặp (EXPLORE / DISCOVER).

- **3 quần xã biên giới**: Đầm Sương Mù (Mistfen Marsh), Thảo Nguyên Chìm
  (Sunken Steppe), Sườn Than Hồng (Ember Ridge). Mỗi quần xã có biến thể hầm mộ
  riêng, thực vật thu hoạch được và 1–2 mob đặc trưng.
- **Trại Viễn Chinh** (làng kiểu mới): các NPC Cartographer / Quartermaster /
  Relic Hunter với hệ **uy tín phe** — nhận commission (giết mob, khảo sát hầm mộ),
  đổi Ancient Shards lấy bản đồ kho báu và công thức hiếm.
- **Nhật ký thám hiểm**: chuỗi advancement + các tấm bia lore trong hầm mộ mở khóa
  trang codex, kể câu chuyện vương quốc đã xây 4 hầm mộ.

## Giai đoạn 3 — "HỘI CỦA VỰC SÂU" (boss & thử thách)

- **4 boss hầm mộ**, mỗi boss đọc đúng "ngôn ngữ thiết kế" của hầm mộ đó:
  - *Warden of the Barracks* — đội hình khiên, phải đánh tạt sườn (mở rộng cơ chế
    đỡ đòn của Copper Shield Skeleton).
  - *The Mine Mother* — đào hang, gọi bầy Gold Digger, sập trần nếu đứng yên.
  - *The Ritual Choir* — phép rune theo nhịp, phải phá đài rune trước.
  - *The Hollow King* — boss cuối, chỉ mở khi có 4 chìa khóa từ 4 hầm mộ.
- **Keystone relics**: vật phẩm boss không chiếm slot relic mà mở khóa
  **bậc rèn mới** (giữ đúng luật tối đa 2 relic chủ động).
- **Chế độ Trial**: hầm mộ remix với modifier (ngập nước, bóng tối, lời nguyền)
  cho phần thưởng shard nhân đôi.

## Giai đoạn 4 — "LÒ RÈN CỦA CÁC THỜI ĐẠI" (rèn & máy móc nhẹ)

- **Alloy Anvil**: trạm việc làm bậc 2 — dung hợp 2 thuộc tính rèn thành
  "Tempered" (ví dụ Sharp+Precision → Keen), tiêu hao shard, có diminishing returns.
- **Nâng cấp relic I → III** bằng shard + nguyên liệu boss.
- **Bánh nước / cối gió** cấp năng lượng tùy chọn cho Master Grindstone
  (tăng tốc nhẹ, không bao giờ bắt buộc — giữ đúng triết lý "không automation").

## Giai đoạn 5 — "TRỜI & BIỂN" (di chuyển)

- **Mô-đun khí cầu**: khoang hàng, ballast, móc neo — mở tuyến buôn bán
  giữa các Trại Viễn Chinh (giá hàng phụ thuộc uy tín).
- **Chuông lặn**: phương tiện lặn cho quần xã Đầm Sương Mù và tàn tích dưới nước.
- HUD khí cầu mở rộng hiển thị mô-đun; giữ khí cầu chậm hơn Elytra như hiện tại.

## Giai đoạn 6 — "THE HOLLOW" (chiều không gian endgame)

- Cổng mở từ Royal Tomb sau khi hạ Hollow King: **The Hollow** — lòng thế giới
  dưới bedrock, nơi 4 phong cách hầm mộ hội tụ thành một đại dungeon liên tục.
- Mob mới, vật liệu rèn độc quyền (Echo Ingot), bàn rèn bậc 3, nhạc disc kỷ nguyên cổ.
- Không thay thế The End — là nhánh tiến trình song song cho người thích khám phá.

---

## Hệ thống ngang suốt các giai đoạn

- **Âm nhạc & ambience** theo độ sâu / quần xã; thêm sound event vanilla-mapped.
- **Config cân bằng** (tỉ lệ drop, cấp rèn, bán kính sprinkler) cho modpack.
- **Ngân sách hiệu năng mỗi giai đoạn**: không quá 2 entity AI mới phức tạp cùng
  lúc, không shader luôn bật, particle giới hạn — như bản 1.0 đã làm.
- **Chất lượng trước**: thứ tự ưu tiên hình ảnh giữ nguyên
  (Grindstone → vũ khí rèn → hầm mộ → relic → khí cầu → bè → tưới tiêu).

> Nguyên tắc chốt: mỗi giai đoạn phải **nối vào vòng lặp cốt lõi**, không thêm hệ
> thống rời rạc. Thà ít mà liền mạch — đó là thứ khiến FRONTIER giống một bản mở
> rộng "chính chủ" của Minecraft.

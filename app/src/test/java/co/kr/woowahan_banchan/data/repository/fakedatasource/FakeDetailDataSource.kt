package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.data.model.remote.response.DetailDataResponse
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse

class FakeDetailDataSource : DetailDataSource {
    override suspend fun getDetail(hash: String): Result<DetailResponse> {
        return Result.success(
            DetailResponse(
                hash = "HF778",
                data = DetailDataResponse(
                    topImageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                    thumbnailUrls = listOf(
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_S.jpg"
                    ),
                    productDescription = "촉촉하게 밴 양념이 일품",
                    point = "260원",
                    deliveryInfo = "서울 경기 새벽 배송 / 전국 택배 배송",
                    deliveryFee = "2,500원 (40,000원 이상 구매 시 무료)",
                    prices = listOf("28,900원", "26,010원"),
                    detailSection = listOf(
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D1.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D2.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/pakage_regular.jpg"
                    )
                )
            )
        )
    }
}
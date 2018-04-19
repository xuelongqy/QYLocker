<template>
  <div id="theme_info">
    <!--主题头部-->
    <div class="tf_head">
      <!--主题名称-->
      <div class="tf_name">主题名称</div>
      <div style="float: right">
        <!--主题作者-->
        <div class="tf_author">KnoYo</div>
        <!--主题日期-->
        <div class="tf_date">2018-01-12</div>
      </div>
    </div>
    <!--主题图片盒子-->
    <div class="tf_images_box">
      <swiper :options="swiperOption" class="images_swiper">
        <swiper-slide>Slide 1</swiper-slide>
        <swiper-slide>Slide 2</swiper-slide>
        <swiper-slide>Slide 3</swiper-slide>
        <swiper-slide>Slide 4</swiper-slide>
        <div class="swiper-pagination" slot="pagination"></div>
      </swiper>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ThemeInfo",
    // 数据
    data() {
      return {
        // 主题信息
        themeInfo: {
          id:-1,
          images: [],
          name: "",
          author: "",
          date: ""
        },
        // 轮播参数
        swiperOption: {
          slidesPerView: 'auto',
          centeredSlides: true,
          spaceBetween: 30,
          pagination: {
            el: '.swiper-pagination',
            clickable: true
          }
        }
      }
    },
    // 页面初始化
    created() {
      // 判断是否已经下载主题
      // 获取主题信息
      // alert(this.$route.params.id)
    }
  }
</script>

<style lang="scss">
  .swiper-slide {
    width: 60%;
    background: red;
  }
  .swiper-slide:nth-child(2n) {
    width: 40%;
  }
  .swiper-slide:nth-child(3n) {
    width: 20%;
  }
  #theme_info {
    height: 100%;
    width: 100%;
    // 主题头部
    .tf_head {
      width: 100%;
      height: 60px;
      padding: 5px 50px;
      // 主题名字
      .tf_name {
        float: left;
        line-height: 50px;
        font-size: 1.4rem;
        font-weight: bold;
      }
      // 主题作者和日期
      .tf_author {
        font-size: 1rem;
      }
      .tf_author,.tf_date {
        line-height: 25px;
      }
    }
    // 主题图片盒子
    .tf_images_box {
      width: 100%;
      height: 200px;
      margin-top: 10px;
      // 轮播器
      .images_swiper {
        height: 100%;
      }
    }
  }
</style>

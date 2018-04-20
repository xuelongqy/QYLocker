<template>
  <div id="theme_info">
    <!--主题头部-->
    <mu-paper class="tf_head" :zDepth="2">
      <!--主题名称-->
      <div class="tf_name">主题名称</div>
      <div style="float: right">
        <!--主题作者-->
        <div class="tf_author">KnoYo</div>
        <!--主题日期-->
        <div class="tf_date">2018-01-12</div>
      </div>
    </mu-paper>
    <!--主题主干区域-->
    <div class="tf_main">
      <!--主题图片盒子-->
      <div class="tf_images_box">
        <swiper :options="swiperOption" class="images_swiper">
          <swiper-slide v-for="(image,index) in themeInfo.images" :key="index">
            <img class="tf_images" :src="image" onerror="src='./static/image/ic_broken_image_grey.png'">
          </swiper-slide>
          <div class="swiper-pagination" slot="pagination"></div>
        </swiper>
      </div>
      <!--主题介绍盒子-->
      <mu-paper class="tf_introduced_box" :zDepth="1">
        <p class="tf_introduced_text">
          {{this.$t("theme.introduced")}}
        </p>
        <p class="tf_introduced">
          一个简单的主题介绍一个简单的主题介绍一个简单的主题介绍一个简单的主题介绍一个简单的主题介绍一个简单的主题介绍
        </p>
      </mu-paper>
    </div>
    <!--主题底部栏-->
    <mu-paper class="tf_footer" :zDepth="2">
      <!--下载按钮-->
      <mu-raised-button label="下载" class="demo-raised-button" primary/>
    </mu-paper>
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
          images: ["http://a.hiphotos.baidu.com/image/pic/item/b64543a98226cffc6ec00edab5014a90f703eaf4.jpg","http://g.hiphotos.baidu.com/image/h%3D300/sign=5a0f156f57b5c9ea7df305e3e538b622/cf1b9d16fdfaaf519d4aa2db805494eef01f7a2c.jpg","http://a.hiphotos.baidu.com/image/h%3D300/sign=c43e07f052df8db1a32e7a643922dddb/0ff41bd5ad6eddc4c984b29335dbb6fd52663372.jpg",""],
          name: "",
          author: "",
          date: ""
        },
        // 轮播参数
        swiperOption: {
          slidesPerView: 'auto',
          spaceBetween: 30,
          pagination: {
            el: '.swiper-pagination',
            clickable: true,
            hide: true
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
    text-align: center;
    background: white;
  }
  #theme_info {
    height: 100%;
    width: 100%;
    // 主题头部
    .tf_head {
      position: absolute;
      top: 0;
      width: 100%;
      height: 60px;
      padding: 5px 50px;
      z-index: 2;
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
    // 主题主干区域
    .tf_main {
      position: absolute;
      top: 60px;
      left: 0;
      right: 0;
      bottom: 60px;
      overflow:auto;
      // 主题图片盒子
      .tf_images_box {
        width: 100%;
        height: 250px;
        margin-top: 10px;
        // 轮播器
        .images_swiper {
          height: 100%;
          // 图片
          .tf_images {
            height: 250px;
            width: auto;
          }
        }
      }
      // 主题介绍盒子
      .tf_introduced_box {
        width: 94%;
        margin: 10px 3%;
        padding: 10px;
        .tf_introduced_text {
          margin: 0;
          font-size: 1rem;
          font-weight: bold;
        }
        .tf_introduced {
          margin: 0;
          text-indent:2em
        }
      }
    }
    // 主题底部栏
    .tf_footer {
      position: absolute;
      bottom: 0;
      width: 100%;
      height: 60px;
      z-index: 2;
      text-align: center;
      line-height: 60px;
      // 主题操作按钮
      .tf_operation_btn {
        width: 50%;
      }
    }
  }
</style>

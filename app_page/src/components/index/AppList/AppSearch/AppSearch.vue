<!--
 @Title: AppSearch.vue
 @Description: App搜索框
 @author XueLong xuelongqy@foxmail.com
 @date 2018/3/1 10:25
 @update_author
 @update_time
 @version V1.0
-->
<template>
  <div id="app_search" :style="app_search_height_style">
    <!--搜索盒子-->
    <div class="as_box" :class="as_box_anim">
      <mu-text-field v-if="isOpen"
                     class="as_search_input"
                     :underlineShow="false"
                     v-model="searchKey"
                     :hintText="$t('appList.search')"/>
      <!--搜索按钮-->
      <mu-icon-button
        class="as_search_btn"
        :icon="isOpen?'close':'search'"
        @click="isOpen = !isOpen"/>
      <!--确定按钮-->
      <mu-icon-button v-if="isOpen & onSearch != null"
                      class="as_search_btn"
                      icon="arrow_forward"
                      @click="onSearchBtn"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: "app-search",
    // 页面启动时
    created() {
    },
    // 数据
    data() {
      return {
        // 是否开启
        isOpen: false,
        // 搜索盒子开关动画
        as_box_anim: "",
        // 搜索栏高度样式
        app_search_height_style: "",
        // 搜索关键字
        searchKey: ""
      }
    },
    // 参数
    props: {
      // 当搜索关键字改变时触发事件
      onSearchKey: {
        type: Function,
        default: function () {}
      },
      onSearch: {
        type: Function,
        default: null
      }
    },
    // 计算方法
    computed: {
      // 获取键盘弹起状态
      keyboardVisible() {
        return this.$store.state.App.keyboardVisible;
      }
    },
    // 监听器
    watch: {
      // 搜索是否开启
      isOpen(newValue, oldValue) {
        // 关闭时清空
        if (!newValue) {
          this.searchKey = ""
        }
        this.as_box_anim = newValue ? 'as_box_open':'as_box_close'
      },
      // 键盘是否开启
      keyboardVisible(newValue, oldValue) {
        if (newValue) this.app_search_height_style = "bottom: " + (this.$store.state.App.keyboardHeight + 5) + "px"
        else this.app_search_height_style = "bottom: 12px"
      },
      // 搜索关键字改变
      searchKey(newValue, oldValue) {
        this.onSearchKey(newValue)
      }
    },
    // 方法
    methods: {
      onSearchBtn() {
        if (this.onSearch != null && this.searchKey != null && this.searchKey !== "") {
          this.onSearch(this.searchKey)
        }
      }
    }
  }
</script>

<style lang="scss">
  #app_search {
    position: absolute;
    bottom: 12px;
    left: 0;
    right: 0;
    padding: 0 20px;
    z-index: 2;

    // App搜索盒子
    .as_box {
      width: 50px;
      height: 50px;
      float: right;
      border-radius: 50%;
      box-shadow: rgba(0, 0, 0, 0.156863) 0 3px 10px, rgba(0, 0, 0, 0.227451) 0 3px 10px;
      background-color: white;

      // 搜索文字输入框
      .as_search_input {
        height: 50px;
        line-height: 50px;
        text-align: center;
        width: calc(100% - 130px);
        margin: 0 0 0 20px;

        // muse-ui部分输入框样式
        .mu-text-field-content {
          height: 34px;
          padding: 8px 0;
          text-align: left;
        }
      }
      // 搜索按钮
      .as_search_btn {
        float: right;
        width: 50px;
        height: 50px;
        color: gray;
      }
    }
    // 开启搜索
    .as_box_open {
      animation: as_box_to_open 0.5s forwards;
    }
      @keyframes as_box_to_open {
        from {
          width: 50px;
          border-radius: 50%;
        }
        to {
          width: 100%;
          border-radius: 4px;
        }
      }
      // 关闭搜索
      .as_box_close {
        animation: as_box_to_close 0.5s forwards;
      }
      @keyframes as_box_to_close {
        from {
          width: 100%;
          border-radius: 4px;
        }
        to {
          width: 50px;
          border-radius: 50%;
        }
      }
  }
</style>

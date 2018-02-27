<template>
  <div id="app_list">
    <!--选项卡-->
    <mu-tabs :value="activeTab" @change="handleTabChange">
      <mu-tab value="tabAll" :title="$t('appList.allApps')"/>
      <mu-tab value="tabSys" :title="$t('appList.sysApps')"/>
      <mu-tab value="tabNonSys" :title="$t('appList.nonSysApps')"/>
    </mu-tabs>
    <!--所有应用-->
    <div class="al_apps_tab" v-if="activeTab === 'tabAll'">
      <!--App条目-->
      <app-item :appName="$t('comm.appName')" :appIcon="'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1519621114671&di=f4fa6f2308863c78388f8610efd87e09&imgtype=0&src=http%3A%2F%2Fsrc.onlinedown.net%2Fnew_img%2Fapk_logo%2F2016%2F0928%2Flogo-for-102990-o_1atns0laepar1jo91q1a4jlnoq-uid-257251.png'"></app-item>
    </div>
    <!--系统应用-->
    <div class="al_apps_tab" v-if="activeTab === 'tabSys'">
      <h2>Tab Two</h2>
      <p>
        这是第二个 tab
      </p>
    </div>
    <!--用户应用-->
    <div class="al_apps_tab" v-if="activeTab === 'tabNonSys'">
      <h2>Tab Three</h2>
      <p>
        这是第三个 tab
      </p>
    </div>
  </div>
</template>

<script>
  import AppItem from './AppItem/AppItem'

  export default {
    name: "AppList",
    // 数据
    data() {
      return {
        // 当前激活的选项卡
        activeTab: 'tabAll'
      }
    },
    // 方法
    methods: {
      // 更换选项卡
      handleTabChange: function(val) {
        this.activeTab = val
      }
    },
    // 组件
    components: {
      'app-item': AppItem
    }
  }
</script>

<style lang="scss" scoped>
  #app_list {
    // 选项卡
    .al_apps_tab {
      position: absolute;
      top: 48px;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 5px 0;
      overflow: auto;
      z-index: -1;
    }
  }
</style>

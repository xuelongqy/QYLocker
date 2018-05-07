<template>
    <div class="setting_item">
      <!--主题设置-->
      <mu-flat-button class="setting_btn">
        <div class="setting_btn">
          <!--图标-->
          <div class="setting_btn_icon" :class="settingIconColor">
            <mu-icon :value="settingIcon" color="white" :size="24"/>
          </div>
          <!--文字盒子-->
          <div class="setting_text_box">
            <!--设置项名字-->
            <p class="setting_name">{{settingName}}</p>
            <!--设置项信息-->
            <p class="setting_info">{{settingInfo}}</p>
          </div>
          <!--开关按钮-->
          <div class="setting_item_switch_box">
            <mu-switch v-if="showSettingSwitch" :value="settingsSwitchValue" @change="onSettingsSwitch"/>
          </div>
        </div>
      </mu-flat-button>
    </div>
</template>

<script>
  export default {
    name: "SettingsItem",
    // 参数
    props: {
      // 设置项名称
      settingName: {
        type: String,
        default: ""
      },
      // 设置项信息
      settingInfo: {
        type: String,
        default: ""
      },
      // 设置图标
      settingIcon: {
        type: String,
        default: ""
      },
      // 图标颜色
      settingIconColor: {
        type: String,
        default: "white"
      },
      // 显示开关
      showSettingSwitch: {
        type: Boolean,
        default: false
      },
      // 开关值
      settingsSwitchValue: {
        type: Boolean,
        default: false
      },
      // 开关值变化
      onSettingsSwitch: {
        type: Function,
        default: function (value) {}
      }
    },
  }
</script>

<style lang="scss">
  .setting_item {
    width: 100%;
    // 设置项按钮
    .setting_btn {
      width: 100%;
      height: 70px;
      // 设置项图标
      .setting_btn_icon {
        float: left;
        margin: 17px;
        height: 36px;
        width: 36px;
        line-height: 36px;
        border-radius: 50%;
        text-align: center;
      }
      // 设置项文字盒子
      .setting_text_box {
        float: left;
        height: 40px;
        margin: 15px 0;
        text-align: left;
        p {
          line-height: normal;
          margin: 0;
          padding: 0;
        }
        //  设置项名字
        .setting_name {
          font-size: 1rem;
        }
        // 设置项信息
        .setting_info {
          font-size: 0.8rem;
          line-height: 1rem;
          width: 200px;
          word-break:break-word;
          color: gray;
        }
      }
      // 开关按钮盒子
      .setting_item_switch_box {
        float: right;
        height: 40px;
        line-height: 40px;
        padding: 15px;
      }
    }
  }

  // 颜色
  .white {
    background: white;
  }
  .orange {
    background: orange;
  }
  .blue {
    background: blue;
  }
  .red {
    background: red;
  }
  .chocolate {
    background: chocolate;
  }
  .coral {
    background: coral;
  }
  .green {
    background: green;
  }
  .blueviolet {
    background: blueviolet;
  }
  .brown {
    background: brown;
  }
</style>

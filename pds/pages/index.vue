<!--<template>
<div>
  <div class="block">
    <span class="demonstration">Défaut</span>
    <el-date-picker
      v-model="value1"
      type="date"
      placeholder="Choississez un jour">
    </el-date-picker>
  </div>
  <div class="block">
    <span class="demonstration">Picker avec raccourcis</span>
    <el-date-picker
      v-model="value2"
      type="date"
      placeholder="Choississez un jour"
      :picker-options="pickerOptions">
    </el-date-picker>
    <el-button @click="widgetAvatar()" outline  type="danger" plain round
               for="fileElem"
               icon="ni ni-image" size="sm">Ajouter la photo de l'enfant
    </el-button>
    <el-image :src="picture"></el-image>
  </div>
  </div>
</template>

<script>
  export default {
    layout : "default",
    middleware: ['authenticated'],
    data() {
      return {
        cloudinary: {
          apiKey: "631914884883475",
          cloudName: "dmpdy8rst",
          uploadPreset: "lrqbzctg"

        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
          file:null,
          picture:'',
          shortcuts: [{
            text: 'Aujourd\'hui',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          }, {
            text: 'Hier',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          }, {
            text: 'Il y a une semaine',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
        },
        value1: '',
        value2: '',
      };
    },
    methods: {
      widgetAvatar(url) {
        console.log(this.picture)
        window.cloudinary.openUploadWidget(
          {
            cloud_name: 'dmpdy8rst',
            upload_preset: 'lrqbzctg',
            theme: 'default',
            maxFiles: 1,
          }

          , (error, result) => {
            console.log(error, result);
            this.file = result[0];

      //      this.picture= `https://res.cloudinary.com/dmpdy8rst/image/asset/w_200,h_200/${this.file.public_id}.jpg`
            this.picture= `https://res.cloudinary.com/dmpdy8rst/image/upload/v1607291953/${this.file.public_id}.jpg`
            console.log(this.picture)


          }
        );

      },
    }
  };
</script>

<style>
  .message-avatar {
    width: 50px;
    height:50px;
    border-radius: 50%;
    border: 1px solid #fcfcfc;
  }
</style>
-->
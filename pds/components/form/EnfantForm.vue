<template>
  <div class="table-container" style="width:100%;">
<el-dialog title="Ajouter un enfant" :visible.sync="dialogFormVisible">
      <el-form :model="modelChild" status-icon :rules="rules" ref="modelChild"  class="demo-ruleForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="" prop="gender" required>
              <el-radio-group v-model="modelChild.gender">
                <el-radio label="Fille"></el-radio>
                <el-radio label="Garçon"></el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col> 
          <el-col :span="7">
            <el-form-item label="" prop="dateOfBirth">
              <el-date-picker placeholder="Date de naissance" type="date" format="yyyy-MM-dd" v-model="modelChild.dateOfBirth" style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col> 
        </el-row>
        <el-form-item label=""  required prop="firstName">
          <el-input v-model="modelChild.firstName" placeholder="Nom de l'enfant"></el-input>
        </el-form-item>
        <el-form-item label=""  required prop="lastName">
          <el-input v-model="modelChild.lastName" placeholder="Prénom de l'enfant"></el-input>
        </el-form-item>
        <el-form-item label=""  required prop="families">
       <el-select
  v-model="modelChild.families"
  placeholder="Selectionnee famille"
>
  <el-option
    v-for="item in families"
    :key="item._id"
    :label="item.fatherFirstName"
    :value="item._id"
    :disabled="item.disabled"
  />
</el-select>
        </el-form-item>
        <el-row>
        </el-row>
       
        <el-form-item style="text-align:right;">
          <el-button  @click="resetForm('modelChild')" plain ><i class="el-icon-close" ></i> Reset</el-button>
          <el-button type="success" @click="addChild('modelChild')"  ><i class="el-icon-check" ></i> Ajouter</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

          <el-button style="float:right;margin-top: 6px;margin-bottom: 10px" type="primary"  @click="dialogFormVisible = true"> <i class="el-icon-plus"></i> Ajouter un enfant</el-button>
       
	<el-table
            :data="children.slice((currentPage-1)*pagesize, currentPage*pagesize).filter(data => !search || data.firstName.toLowerCase().includes(search.toLowerCase()))" class="shadow-table">

            <el-table-column
              label="Father name"
              prop="families"
             >
            </el-table-column>
            
            <el-table-column
              label="Prénom"
              prop="firstName"
             >
            </el-table-column>

          <el-table-column
              label="Nom"
              prop="lastName"
             >
            </el-table-column>
            <el-table-column
              label="Sexe"
              prop="gender"
             >
            </el-table-column>
            <el-table-column
              label="Date de naissance"
              prop="dateOfBirth"
            >
              <template slot-scope="scope">
                {{ toReadable(scope.row.dateOfBirth) }}
              </template>
            </el-table-column>

            <el-table-column
              label="Date de création"
              prop="creationDate"
            >
              <template slot-scope="scope">
                {{ toReadable(scope.row.creationDate) }}
              </template>
            </el-table-column>

            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  placeholder="Trouver un enfant .."/>
              </template>
              <template scope="scope">
                <el-tooltip class="item" content="Modifier cet enfant" placement="top-start">
                  <el-button
                    type="success"
                    icon="el-icon-edit"
                    circle
                    @click="handleEdit(scope.$index,scope.row)" >
                  </el-button>
                </el-tooltip>
                
              </template>
            </el-table-column>
          </el-table>

      <!-- Pagination -->
      <el-row align="center">
        <el-col :span="24" align="center">
          <br/>
          <el-pagination
            background
            :page-size="5"
            :pager-count="5"
            layout="prev, pager, next"
            :total="children.length"
            @current-change="current_change">
          </el-pagination>
        </el-col>
      </el-row>
	</div>
	</template>
   <script>
    import {createNamespacedHelpers} from 'vuex'
    import { mapGetters ,mapActions,mapState} from 'vuex'
    import moment from 'moment'
    import axios from 'axios';
 
    export default {
        
        data() {
          return {
                total: 0,
                pagesize:5,
                currentPage:1,
       
                listLoading: false,
                dialogTableVisible: false,
                dialogFormVisible: false,
                dialogFormUpdateVisible: false,
                dialogVisible: false,
                dialogDeleteVisible: false,
                modelChild: {
               
                    firstName: '',
                    lastName: '',
                    gender: '',
                    dateOfBirth: '',
                    families: '', 
                },
                
                formLabelWidth: '120px',
                search: '',
                preview: null
          }


        },
        mounted() {
               this.$store.dispatch('child/FETCH_CHILDREN'),
                this.$store.dispatch('family/FETCH_FAMILIES')
             

        },

        computed: {
            ...mapState(
                {  children: state => state.child.children,
                families: state => state.family.families
                }
                    
            ),
     
            ...mapGetters(['child/CHILDREN']),
            ...mapGetters(['family/FAMILIES'])

        },
        methods: {
            ...mapActions({
                add: "child/ADD_CHILD",
                delete:"child/DELETE_CHILD",
                update:"child/UPDATE_CHILD"},
            ),
            addChild(modelChild) {
               
                    this.$refs[modelChild].validate((valid) => {
                        if (valid) {

                            try {
                                this.$store.dispatch('child/ADD_CHILD', {

                                    payload: this.modelChild


                                }).then(() => {

                                    this.$notify({
                                        title: 'Success',
                                        message: 'Un enfant est ajouté avec succès !',
                                        type: 'success'
                                    });
                                    this.resetForm(modelChild)
                                    this.dialogFormVisible = false

                                })
                            }
                            catch (e) {


                                this.$notify({
                                    title: 'Erreur',
                                    message: 'Une erreur c\'est produite!',
                                    type: 'error'
                                });

                            }
                        }

                        else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
            },
            updateChild(editChild){
                
                    this.$refs[editChild].validate((valid) => {
                        if (valid) {
                            this.update(this.editChild)
                            this.$notify({
                                title: 'Succès ',
                                message: 'Enfant  modifée avec succés!',
                                type: 'success'
                            });
                            this.dialogFormUpdateVisible = false

                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
            },

            deleteChild(){
                this.delete(this.formModelChild)
                this.$message({
                    message: 'Enfant supprimé avec avec succès !',
                    type: 'error'
                });         this.dialogDeleteVisible = false
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();

            },
            handleEdit(index, row) {
                console.log(index, row);
                this.dialogFormUpdateVisible = true;
                this.editChild = Object.assign({}, row);
            },
            handleRead(index, row) {
                console.log(index, row);
                this.dialogVisible = true;
                this.editChild = Object.assign({}, row);
            },
            handleDelete(index, row) {
                this.formModelChild=Object.assign({}, row);
                this.dialogDeleteVisible=true
            },
            current_change:function(currentPage){
                this.currentPage = currentPage;
            },
            toReadable: (date) => {
                return moment(date).format('DD/MM/YYYY')
            },
           
      
        }

    }
</script>
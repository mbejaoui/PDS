export const state = () => {
  return {
    children: [],
    isBusy: true,
    error: null
  }
}

export const getters = {
    CHILDREN: state => {
      return state.children
    },
    
    IS_BUSY: state => {
      return state.isBusy
    },
  
  ERROR: state => {
      return state.error
    }
}
export const mutations = {
  SET_CHILDREN: (state, children) => {
    state.children = children
    state.isBusy = false
  },
  
  SET_IS_BUSY: (state, isBusy) => {
    state.isBusy = isBusy
  },
  SET_ERROR: (state, error) => {
    state.error = error
  },
 

}

export const actions =  {
  async FETCH_CHILDREN(context) {
    context.commit('SET_IS_BUSY', true)
    try {
      const {data} = await this.$axios.get("/children")
      context.commit('SET_CHILDREN', data)
    } catch(err) {
      context.commit('SET_IS_BUSY', false)
      context.commit('SET_ERROR', err)
    }
  },
  async ADD_CHILD(context,{payload }) {
   console.log(payload +"ahla")
    context.commit('SET_IS_BUSY', true)
    try {
      await this.$axios.post('/children',payload)
      const {data} = await this.$axios.get("/children")
      context.commit('SET_CHILDREN', data)
   
    } catch(err) {
      context.commit('SET_IS_BUSY', false)
      context.commit('SET_ERROR', err)
    }
  },
  /*
  async UPDATE_CHILD(context,payload ) {
    context.commit('SET_IS_BUSY', true)
    try {
      await this.$axios.post(/children/${payload._id},payload)
       const {data} = await this.$axios.get("/childrens")
      context.commit('SET_CHILDREN', data)
    } catch(err) {
      context.commit('SET_IS_BUSY', false)
      context.commit('SET_ERROR', err)
    }
  },
  async DELETE_CHILD(context,payload) {
    context.commit('SET_IS_BUSY', true)
    try {
      await this.$axios.delete(/children/deactivate-child/${payload._id})
       const {data} = await this.$axios.get("/childrens")
       context.commit('SET_CHILDREN', data)
    } catch(err) {
      context.commit('SET_IS_BUSY', false)
      context.commit('SET_ERROR', err)
    }
  }
*/
}
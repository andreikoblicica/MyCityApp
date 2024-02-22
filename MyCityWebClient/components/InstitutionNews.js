import React, {useEffect, useState} from "react";
import {
 Box,
 Button,
 Dialog,
 DialogActions,
 DialogContent,
 DialogTitle,
  Input,InputLabel,
 TextField,
 Typography
} from '@mui/material'
import IconButton from "@mui/material/IconButton";
import {DataGrid} from "@mui/x-data-grid";
import {Delete,Visibility, Add, Edit} from '@mui/icons-material/';
import {news} from "../services/news"

import {useNavigate} from "react-router-dom";

import moment from "moment/moment";
import {fileUploader} from "../services/fileUploader";



const InstitutionNews = () =>{
 const user = JSON.parse(sessionStorage.getItem('user'));
 const institution = JSON.parse(sessionStorage.getItem('institution'));
 const [selectedNews, setSelectedNews] = useState([])
 const [refreshKey, setRefreshKey] = useState(0);
 const [openViewDialog, setOpenViewDialog] = useState(false)
 const [openCreateDialog, setOpenCreateDialog] = useState(false)
 const [dialogTitle,setDialogTitle]=useState("")

 const [formTitle,setFormTitle]=useState("")
 const [formLink,setFormLink]=useState("")
 const [formDescription,setFormDescription]=useState("")
 const [formImageFile, setFormImageFile]=useState(null)
 const [formImageName, setFormImageName]=useState("")

 const [allNews, setAllNews]=useState([])


 const [websiteError,setWebsiteError]=useState(false)
 const [descriptionError,setDescriptionError]=useState(false)
 const [descriptionHelper,setDescriptionHelper]=useState("")
 const [websiteHelper,setWebsiteHelper]=useState("")

 const [formTitleError,setFormTitleError]=useState(false)
 const [formTitleHelper,setFormTitleHelper]=useState("")
 const [isFormValid,setIsFormValid]=useState(true)

 const [openConfirmDialog,setOpenConfirmDialog]=useState(false)

 const navigate=useNavigate()


 const columns=[
  {
   field: "id",
   headerName: "ID",
   flex:0.05,
  },
  {
   field: "title",
   headerName: "Title",
   flex:0.45
  },
  {
   field: "dateTime",
   headerName: "Date",
   flex:0.15
  },
  {
   field: "websiteLink",
   headerName: "Link",
   flex:0.15
  },
  {
   field: 'actions',
   headerName: 'Actions',
   flex:0.1,
   sortable: false,
   renderCell: (params) => (
       <>
        <IconButton aria-label="view"  onClick={() => {
         viewNews(params.row)}
        }>
         <Visibility />
        </IconButton>
        <IconButton aria-label="edit"  onClick={() => {
         editNews(params.row)}
        }>
         <Edit />
        </IconButton>
        <IconButton aria-label="delete"  onClick={() => {
          setSelectedNews(params.row)
          setOpenConfirmDialog(true)
        }}>
         <Delete />
        </IconButton>
       </>
   ),
   params: (params) => ({ ...params, row: params.row }),
  },
 ]

 useEffect(() => {
  if(!user){
   navigate("/")
  }
  async function getAllNews(){
   const res= await news.findByInstitution(institution.id);
   setAllNews(res);
  }

  getAllNews();
 }, [refreshKey])



 const viewNews = (news)=>{
  setSelectedNews(news)
  setOpenViewDialog(true)
 }

 const editNews=(news)=>{
  setDialogTitle("Update News")
  setFormTitle(news.title)
  setFormDescription(news.description)
  setFormImageName(news.image)
  setFormLink(news.websiteLink)
  setOpenCreateDialog(true)
 }



 const confirmDelete =()=>{
  news.delete(selectedNews.id).then(()=>{
   setRefreshKey(refreshKey+1)
   setOpenConfirmDialog(false)
  })
 }


 const createNews = () =>{
  setDialogTitle("Add News")
  setOpenCreateDialog(true)
 }
 const cancelCreate = () =>{
  setOpenCreateDialog(false)
  setFormTitle("")
  setFormDescription("")
  setFormImageName("")
  setFormLink("")
  setFormImageFile(null)
  setDescriptionError(false)
  setDescriptionHelper("")
  setWebsiteError(false)
  setWebsiteHelper("")
  setIsFormValid(true)
  setFormTitleError(false)
  setFormTitleHelper("")
 }
 const submit = () => {
  let newsDTO = {
   title: formTitle,
   description: formDescription,
   image: "",
   websiteLink: formLink,
   institution: institution.name,
   dateTime: moment().format('YYYY-MM-DD HH:mm:ss')
  }
  if (validForm()) {
   const file = {
    preview: URL.createObjectURL(formImageFile),
    data: formImageFile,
   }
   fileUploader.handleSubmit(file).then(publicUrl => {
    newsDTO.image = publicUrl
    news.create(newsDTO).then(() => {
     cancelCreate()
     setRefreshKey(refreshKey + 1)
    })
   })


  } else {
   setIsFormValid(false)
  }
 }

  const validForm = () => {
   return !formTitleError&&!descriptionError&&!websiteError&&formTitle!==''&&formDescription!==''&&formLink!==''

  }

  const handleTitleBlur= ()=>{
   let helperText=''
   let error=false
   if(formTitle.length<1){
    helperText="Title cannot be empty"
    error=true
   }
   setFormTitleError(error)
   setFormTitleHelper(helperText)
  }

  const handleDescriptionBlur=()=>{
   let helperText=''
   let error=false
   if(formDescription.length<1){
    helperText="Description cannot be empty"
    error=true
   }
   setDescriptionError(error)
   setDescriptionHelper(helperText)
  }

  const handleWebsiteBlur=()=>{
   let helperText=''
   let error=false
   const websiteRegex=/^https?:\/\/(?:www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b(?:[-a-zA-Z0-9()@:%_\+.~#?&\/=]*)$/

   if(!websiteRegex.test(formLink)){
    helperText="Website link is not valid"
    error=true
   }
   setWebsiteError(error)
   setWebsiteHelper(helperText)
  }


  const close=()=>{
  setOpenViewDialog(false)
 }



 return (
     user &&<Box>
      <Box mt="90px" ml="290px">
       <Box mb="20px">
        <Typography
            variant="h5"
            fontWeight="bold"
            color="#192841"
        >
         News
        </Typography>
        <Typography variant="body1" color="text.secondary"  >
         Manage Institution News
        </Typography>
        <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'right', width:"97%"}}>
         <Button variant="contained" onClick={()=>createNews()}>
          <Add /> Create
         </Button>
        </Box>


       </Box>




       <Box height="475px" width="97%"
            sx={{
             "& .MuiDataGrid-root": {
              border: "1px solid #aaaaaa !important",
             },
             "& .MuiDataGrid-cell": {
              borderBottom: "1px solid #bbbbbb !important",

             },
             "& .MuiDataGrid-columnHeaders": {
              borderBottom: "1px solid #aaaaaa !important",
              // backgroundColor: "#cccccc",
             },
            }}
       >
        <DataGrid rows={allNews}
                  columns={columns}
                  pageSize={10}
                  rowsPerPageOptions={[10]}
                  onRowClick={(rowInfo) =>
                  { setSelectedNews(rowInfo.row)}}/>

       </Box>


       <Dialog open={openCreateDialog} maxWidth="900px" >
        <DialogTitle sx={{ml:1}}>{dialogTitle}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"900px",p:"0 35px 10px 35px"}}>
         <TextField
             margin="dense"
             id="title"
             label="Title"
             type="text"
             fullWidth
             variant="outlined"
             size="small"
             error={formTitleError}
             onBlur={handleTitleBlur}
             helperText={formTitleHelper}

             value={formTitle}
             onChange={(e) => setFormTitle(e.target.value)}
         />


         <TextField
             multiline
             margin="dense"
             id="description"
             label="Description"
             type="text"
             fullWidth
             variant="outlined"
             size="small"
             value={formDescription}
             onChange={(e) => setFormDescription(e.target.value)}

             error={descriptionError}
             onBlur={handleDescriptionBlur}
             helperText={descriptionHelper}
         />

         <TextField
             margin="dense"
             id="link"
             label="Website Link"
             type="text"
             fullWidth
             variant="outlined"
             size="small"
             value={formLink}
             onChange={(e) => setFormLink(e.target.value)}

             error={websiteError}
             onBlur={handleWebsiteBlur}
             helperText={websiteHelper}
         />

         <Box  border={1} borderRadius={1} borderColor="#bbbbbb" height="40px" mt="10px">
          <InputLabel htmlFor="upload-file"  sx={{pl:"15px", pt:"8px"}}>
           {formImageName==="" ? "Upload Picture" : formImageName}
          </InputLabel>
          <Input
              id="upload-file"
              type="file"
              inputProps={{ style: { display: "none" } }}
              onChange={e=>{setFormImageFile(e.target.files[0]);setFormImageName(e.target.files[0].name)}}
          />
         </Box>
         <Typography color="red" sx={{ml:"5px"}}>{isFormValid ? '' : 'Please make sure all fields are valid!'}</Typography>
        </DialogContent>
        <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
         <Button variant="contained" onClick={cancelCreate}>Cancel</Button>
         <Button variant="contained" onClick={submit}>Submit</Button>
        </DialogActions>
       </Dialog>


      </Box>
      <Dialog open={openViewDialog} maxWidth="900px">
       <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"900px",p:"0 0 10px 0px"}}>
        <Box
            component="img"
            sx={{
             width:"100%",
             height:"300px"
            }}
            alt="Image"
            src={selectedNews.image}
        />
        <Box ml="20px" mr="20px">
         <Typography variant="h5" component="div" sx={{mb:1}}>
          {selectedNews.title}
         </Typography>
         <Box display="flex" justifyContent="space-between" mb={2} mr={1}>
          <Typography variant="subtitle1" color="text.secondary" component="div">
           {selectedNews.institution}
          </Typography>
          <Typography variant="subtitle1" color="text.secondary" component="div">
           {selectedNews.dateTime}
          </Typography>
         </Box>
         <Typography variant="body1" component="div">
          {selectedNews.description}
         </Typography>
        </Box>


       </DialogContent>
       <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
        <Button variant="contained" onClick={close}>Close</Button>
        <Button variant="contained" onClick={confirmDelete}>Delete</Button>
       </DialogActions>
      </Dialog>

      <Dialog open={openConfirmDialog} >
       <DialogTitle>Delete news?</DialogTitle>
       <DialogContent>
        Once you confirm this action, the news will be permanently deleted
       </DialogContent>
       <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
        <Button variant="contained" onClick={()=>setOpenConfirmDialog(false)}>Cancel</Button>
        <Button variant="contained" onClick={confirmDelete}>Delete</Button>
       </DialogActions>
      </Dialog>

     </Box>

 )
}

export default InstitutionNews;
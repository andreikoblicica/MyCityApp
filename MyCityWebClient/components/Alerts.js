import React, {useEffect, useState} from "react";
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    TextField,
    Typography
} from '@mui/material'
import IconButton from "@mui/material/IconButton";
import {DataGrid} from "@mui/x-data-grid";
import {Delete,Visibility, Add} from '@mui/icons-material/';
import {useNavigate} from "react-router-dom";
import {alerts} from "../services/alerts";
import moment from "moment";

const Alerts = () =>{
    const user = JSON.parse(sessionStorage.getItem('user'));
    const [refreshKey, setRefreshKey] = useState(0);
    const [selectedAlert, setSelectedAlert] = useState([])
    const [openCreateDialog, setOpenCreateDialog] = useState(false)
    const [openViewDialog, setOpenViewDialog]=useState(false)
    const [formTitle,setFormTitle]=useState("")
    const [formDescription,setFormDescription]=useState("")
    const [allAlerts, setAllAlerts]=useState([])
    const [isFormValid,setIsFormValid]=useState(true)

    const [titleError, setTitleError]=useState(false)
    const [descriptionError, setDescriptionError]=useState(false)

    const [titleHelper, setTitleHelper]=useState(false)
    const [descriptionHelper, setDescriptionHelper]=useState(false)

    const [openConfirmDialog,setOpenConfirmDialog]=useState(false)
    const navigate=useNavigate()



    const columns=[
        {
            field: "id",
            headerName: "ID",
            flex:0.1
        },
        {
            field: "institution",
            headerName: "Institution",
            flex:0.3
        },

        {
            field: "title",
            headerName: "Title",
            flex:0.3
        },
        {
            field: "dateTime",
            headerName: "Time",
            flex:0.2
        },
        {
            field: 'actions',
            headerName: 'Actions',
            width: 150,
            sortable: false,
            flex:0.1,
            renderCell: (params) => (
                <>
                    <IconButton aria-label="edit"  onClick={() => {
                        viewAlert(params.row)}
                    }>
                        <Visibility />
                    </IconButton>
                    <IconButton aria-label="delete"  onClick={() => {
                        setSelectedAlert(params.row)
                        deleteAlert()
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
        async function getAllAlerts(){
            const res= await alerts.findAll();
            setAllAlerts(res);
        }

        getAllAlerts();
    }, [refreshKey])


    const confirmDelete =()=>{
        alerts.delete(selectedAlert.id).then(()=> {
            setRefreshKey(refreshKey + 1)
            setOpenConfirmDialog(false)
        });
    }

    const deleteAlert=()=>{
        setOpenConfirmDialog(true)
    }

    const viewAlert=(alert)=>{
        setSelectedAlert(alert)
        setOpenViewDialog(true)
    }




    const cancel = () =>{
        setOpenCreateDialog(false)
        setFormTitle("")
        setIsFormValid(true)
        setFormDescription("")
        setTitleError(false)
        setTitleHelper("")
        setDescriptionError(false)
        setDescriptionHelper("")
    }

    const close=()=>{
        setOpenViewDialog(false)

    }
    const submit = () =>{
        let alert={
            institution: "",
            title: formTitle,
            description: formDescription,
            dateTime: moment().format('YYYY-MM-DD HH:mm')
        }
        if(validForm()){
            alerts.create(alert).then(e=>{
                setRefreshKey(refreshKey+1)
                setOpenCreateDialog(false)
                cancel();
            })
        }else{
            setIsFormValid(false)
        }

    }

    const validForm=()=>{
        return !titleError&&!descriptionError&&formTitle!==''&&formDescription!==''
    }

    const handleTitleBlur=()=>{
        let helperText=''
        let error=false
        if(formTitle.length<1){
            helperText="Title cannot be empty"
            error=true
        }
        setTitleHelper(helperText)
        setTitleError(error)
    }

    const handleDescriptionBlur=()=>{
        let helperText=''
        let error=false
        if(formDescription.length<1){
            helperText="Description cannot be empty"
            error=true
        }
        setDescriptionHelper(helperText)
        setDescriptionError(error)
    }

    return (
        user && <Box>
            <Box mt="90px" ml="290px">
                <Box mb="20px">
                    <Typography
                        variant="h5"
                        fontWeight="bold"
                        color="#192841"
                    >
                        Alerts
                    </Typography>
                    <Typography variant="body1" color="text.secondary"  >
                        Manage Institution Alerts
                    </Typography>
                    <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'right', width:"97%"}}>
                        <Button variant="contained" onClick={()=>setOpenCreateDialog(true)}>
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
                    <DataGrid rows={allAlerts}
                              columns={columns}
                              pageSize={10}
                              rowsPerPageOptions={[10]}
                              onRowClick={(rowInfo) =>
                              { setSelectedAlert(rowInfo.row)}}/>
                </Box>





            </Box>

            <Dialog open={openCreateDialog} >
                <DialogTitle sx={{ml:1}}>Create Alert</DialogTitle>
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"500px",p:"0 35px 10px 35px"}}>
                    <TextField
                        margin="dense"
                        id="title"
                        label="Title"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={titleError}
                        onBlur={handleTitleBlur}
                        helperText={titleHelper}
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
                        error={descriptionError}
                        onBlur={handleDescriptionBlur}
                        helperText={descriptionHelper}
                        value={formDescription}
                        onChange={(e) => setFormDescription(e.target.value)}
                    />
                    <Typography color="red">{isFormValid ? '' : 'Please make sure all fields are valid!'}</Typography>

                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={cancel}>Cancel</Button>
                    <Button variant="contained" onClick={submit}>Submit</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openViewDialog} maxWidth="900px">
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"500px",p:"0 0 10px 0px", m:"15px"}}>
                    <Box ml="20px" mr="20px">
                        <Typography variant="h5" component="div" sx={{mb:"5px"}}>
                            {selectedAlert.title}
                        </Typography>
                        <Box display="flex" justifyContent="space-between" mb="20px" mr={1}>
                            <Typography variant="subtitle1" color="text.secondary" component="div">
                                {selectedAlert.institution}
                            </Typography>
                            <Typography variant="subtitle1" color="text.secondary" component="div">
                                {selectedAlert.dateTime}
                            </Typography>
                        </Box>
                        <Typography variant="body1" component="div">
                            {selectedAlert.description}
                        </Typography>
                    </Box>


                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={close}>Close</Button>
                    <Button variant="contained" onClick={(e)=>confirmDelete()}>Delete</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openConfirmDialog} >
                <DialogTitle>Delete alert?</DialogTitle>
                <DialogContent>
                    Once you confirm this action, the alert will be permanently deleted
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={()=>setOpenConfirmDialog(false)}>Cancel</Button>
                    <Button variant="contained" onClick={confirmDelete}>Delete</Button>
                </DialogActions>
            </Dialog>
        </Box>

    )
}

export default Alerts;
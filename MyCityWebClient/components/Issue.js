import React, {useEffect, useRef, useState} from "react";
import {useNavigate, useParams} from 'react-router-dom';
import SockJsClient from 'react-stomp';
import {
    Box,
    Button,
    Checkbox,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    InputLabel,
    ListItemText,
    MenuItem,
    OutlinedInput,
    Select, TextField,
    Typography
} from '@mui/material'
import {DataGrid} from "@mui/x-data-grid";
import {FiberManualRecord, LocationOn, Schedule, Delete} from '@mui/icons-material/';
import {issues} from "../services/issues";
import {institutions} from "../services/institutions";
import Map from "./Map"

const Issue=()=>{

    const {id}=useParams();

    const user=JSON.parse(sessionStorage.getItem('user'))
    const institution=JSON.parse(sessionStorage.getItem('institution'))
    const [issue,setIssue]=useState([])
    const [refreshKey,setRefreshKey]=useState(0)
    const [allInstitutions,setAllInstitutions]=useState([])
    const [selectedShareInstitutions,setSelectedShareInstitutions]=useState([])
    const [reassignInstitution,setReassignInstitution]=useState("")
    const [involvedInstitutions,setInvolvedInstitutions]=useState([])

    const [openReassignDialog,setOpenReassignDialog]=useState(false)
    const [openShareDialog,setOpenShareDialog]=useState(false)

    const [status,setStatus]=useState("")
    const [statusDotColor,setStatusDotColor]=useState("")
    const [assignButtonText,setAssignButtonText]=useState("")
    const [solvedButtonText,setSolvedButtonText]=useState("")
    const [assignButtonVariant,setAssignButtonVariant]=useState("contained")
    const [solvedButtonVariant,setSolvedButtonVariant]=useState("contained")

    const [involvement,setInvolvement]=useState([])

    const [openSuccessDialog,setOpenSuccessDialog]=useState(false)
    const [successDialogTitle,setSuccessDialogTitle]=useState("")
    const [successDialogDescription,setSuccessDialogDescription]=useState("")

    const [openDeleteDialog,setOpenDeleteDialog]=useState(false)

    const [coordinates,setCoordinates]=useState([])

    const [newMessage, setNewMessage]=useState([])
    const [messages,setMessages]=useState([])

    const [topics,setTopics]=useState([])


    const [isDisabledDelete,setIsDisabledDelete]=useState(false)
    const [isDisabledSolve,setIsDisabledSolve]=useState(false)
    const [isDisabledAssign,setIsDisabledAssign]=useState(false)
    const [isDisabledReassign,setIsDisabledReassign]=useState(false)
    const [isDisabledShare,setIsDisabledShare]=useState(false)
    const [isDisabledReassignField,setIsDisabledReassignField]=useState(false)
    const [isDisabledShareField,setIsDisabledShareField]=useState(false)
    const [isDisabledChat,setIsDisabledChat]=useState(false)
    const [isDisabledSend,setIsDisabledSend]=useState(false)

    const navigate=useNavigate();

    const chatRef = useRef(null);

    const statusColors = {
        opened: '#ed2938',
        inProgress: '#ffaa1c',
        solved: '#006b3e'
    };

    const columns=[
        {
            field: "id",
            headerName: "ID",
            flex:0.1
        },
        {
            field: "institutionName",
            headerName: "Institution",
            flex:0.6
        },
        {
            field: "issueStatus",
            headerName: "Status",
            flex:0.3
        },

    ]

    useEffect(() => {
        const containerElement = chatRef.current;
        containerElement.scrollTop = containerElement.scrollHeight;
    }, [messages]);

    useEffect(() => {
        if(!user){
            navigate("/")
        }
        async function init(){
            const issue= await issues.findById(id);
            const res=await institutions.findAll()
            setIssue(issue)
            setMessages(issue.messages)
            const [latitude,longitude]=issue.coordinates.split(",")
            const coordinates={
                lat: parseFloat(latitude),
                lng: parseFloat(longitude)
            }
            setCoordinates(coordinates)
            setInvolvedInstitutions(issue.involvedInstitutions)
            setAllInstitutions(res.filter((item)=>!issue.involvedInstitutions.some(item2=>item.name===item2.institutionName)))
            const involvement= issue.involvedInstitutions.filter(item=>item.institutionName===institution.name).at(0)
            setInvolvement(involvement)
            const status=involvement.issueStatus

            initButtons(status)
            setStatusDotColor(getDotColor(issue))

            if(issue.status==='In Progress' && issue.involvedInstitutions.every(element=>element.issueStatus==='Solved')){
                issue.status = 'Solved'
                issues.update(issue).then(() => setRefreshKey(refreshKey + 1))
            }
            setStatus(status)
            disable(involvement,issue);
        }

        init();

    }, [refreshKey])

    const disable=(involvement,issue)=>{
        if(involvement.username!==user.username){
            setIsDisabledChat(true)
            setIsDisabledSend(true)
            setIsDisabledSolve(true)
            if(involvement.username!==null){
                setIsDisabledShare(true)
                setIsDisabledReassign(true)
                setIsDisabledShareField(true)
                setIsDisabledReassignField(true)
                setIsDisabledAssign(true)
                setIsDisabledDelete(true)
            }
        }
        if(issue.status==='Solved'){
            setIsDisabledShare(true)
            setIsDisabledReassign(true)
            setIsDisabledShareField(true)
            setIsDisabledReassignField(true)
            setIsDisabledChat(true)
            setIsDisabledSend(true)
        }

    }


    const getDotColor=(issue)=>{
        let color=""
        if(issue.status==='Opened'){
            color=statusColors.opened
        }else if(issue.status==='In Progress'){
            color=statusColors.inProgress
        }else{
            color=statusColors.solved
        }
        return color
    }

    const initButtons=(status)=>{
        let assignVariant=""
        let assignText=""
        let solvedVariant=""
        let solvedText=""
        if(status==='Opened'){
            assignVariant="contained"
            assignText="assign to you"
            solvedVariant="contained"
            solvedText="mark as solved"
        }else{
            if(status==='In Progress'){
                assignVariant="outlined"
                assignText="assigned"
                solvedVariant="contained"
                solvedText="mark as solved"
            }else{
                solvedVariant="outlined"
                solvedText="solved"
                assignText="assigned"
                assignVariant="outlined"
            }
        }
        setAssignButtonVariant(assignVariant)
        setAssignButtonText(assignText)
        setSolvedButtonText(solvedText)
        setSolvedButtonVariant(solvedVariant)
    }

    const handleAssignClick=()=>{
        if(status==='Opened'){
            let involvedInstitution={
                id:involvement.id,
                institutionName:"",
                username:user.username,
                issueStatus:"In Progress"
            }
            setIsDisabledChat(false)
            setIsDisabledSend(false)
            setIsDisabledSolve(false)
            institutions.updateInvolvedInstitution(involvedInstitution).then(
                ()=>{
                    setAssignButtonVariant("outlined")
                    setAssignButtonText("assigned")
                    setRefreshKey(refreshKey+1)
                }
            )
            if(issue.status==='Opened'){
                issue.status='In Progress'
                issues.update(issue).then(()=>setRefreshKey(refreshKey+1))

            }
        }
    }

    const handleSolvedClick=()=>{
        if(status==='In Progress'){
            let involvedInstitution={
                id:involvement.id,
                institutionName:"",
                username:user.username,
                issueStatus:"Solved"
            }
            setIsDisabledReassignField(true)
            setIsDisabledShareField(true)
            setIsDisabledReassign(true)
            setIsDisabledShare(true)
            institutions.updateInvolvedInstitution(involvedInstitution).then(
                ()=>{
                    setSolvedButtonVariant("outlined")
                    setSolvedButtonText("solved")
                    setRefreshKey(refreshKey+1)
                }
            )

        }
    }


    const handleShareChange = (event) => {
        const {
            target: { value },
        } = event;
        setSelectedShareInstitutions(
            typeof value === 'string' ? value.split(',') : value,
        );
    };

    const cancelReassign=()=>{
        setOpenReassignDialog(false)
        setReassignInstitution("")
    }

    const submitReassign=()=>{
        let involvedInstitution={
            id:null,
            institutionName: reassignInstitution,
            username:null,
            issueStatus:"Opened",
        }
        institutions.reassignIssue(involvedInstitution,issue.id).then(()=>{
            setOpenReassignDialog(false)
            setSuccessDialogTitle("Issue reassigned successfully")
            setSuccessDialogDescription("Close this dialog to navigate back to the main page")
            setOpenSuccessDialog(true)
        })
    }

    const handleReassignClick=()=>{
        if(reassignInstitution!==""){
            setOpenReassignDialog(true)
        }
    }

    const cancelShare=()=>{
        setOpenShareDialog(false)
        setSelectedShareInstitutions([])
    }

    const submitShare=()=>{
        let newInstitutions=selectedShareInstitutions.map(element=>({
            id:null,
            institutionName:element,
            username:null,
            issueStatus:"Opened"
        }))
        institutions.shareIssue(newInstitutions,issue.id).then(()=> {
                setOpenShareDialog(false)
                setSuccessDialogTitle("Issue shared successfully!")
                setSuccessDialogDescription("Close this dialog to return to the issue page")
                setOpenSuccessDialog(true)
                setSelectedShareInstitutions([])
                setRefreshKey(refreshKey + 1)
            }
        )
    }

    const handleShareClick=()=>{
        if(selectedShareInstitutions.length!==0){
            setOpenShareDialog(true)
        }
    }



    const closeSuccessDialog=()=>{
        setOpenSuccessDialog(false)
        if(successDialogTitle==="Issue reassigned successfully"){
            navigate('/institution')
        }else{
            setRefreshKey(refreshKey+1)
        }
    }

    const deleteIssue=()=>{
        issues.delete(issue.id)
        setOpenDeleteDialog(false)
        navigate('/institution')
    }

    const showNewMessage=(message)=>{
        setMessages(messages=>[...messages,message])
    }

    const onConnect=()=>{
        console.log("Connected!")
        setTopics(['/topic/messages/'+issue.id+"/"+institution.name])
    }

    const onDisconnect=()=>{
        console.log("Disconnected!")
    }

    const sendMessage=()=>{

        const destinationNames=involvedInstitutions.map(involvedInstitution=>involvedInstitution.institutionName).filter(name=>name!==institution.name)
        destinationNames.push(issue.username)
        const message={
            issueId:issue.id,
            sourceName:institution.name,
            destinationNames: destinationNames,
            message:newMessage,
            timestamp: formatDate(new Date())
        }
        issues.sendMessage(message).then(res=>{
            setMessages(messages=>[...messages,message])
            setNewMessage("")
        })

    }

    const formatDate = (date) => {
        const options = {
            day: 'numeric',
            month: 'short',
            hour: 'numeric',
            minute: 'numeric',
            hour12: false
        };

        return date.toLocaleString('en-US', options);
    };



    return(
        user &&<Box mt="100px" ml="290px">

            <Box mb="20px">
                <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'space-between', width:"97%", alignItems:"center"}}>
                    <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'left', width:"40%"}}>
                        <Typography

                            variant="body1"
                            color="white"
                            bgcolor="#192841"
                            borderRadius="5px"
                            mr="10px"
                            pl="10px"
                            pr="10px"
                            sx={{display:'flex',alignItems:'center'}}
                        >Issue #{issue.id}
                        </Typography>
                        <Typography  variant="h5" fontWeight="bold" color="#192841">{issue.title}</Typography>
                    </Box>
                    <Box  sx={{display: 'flex', flexDirection:'row', justifyContent:'right', width:"40%"}}>
                       <Button disabled={isDisabledAssign} variant={assignButtonVariant} color="primary" sx={{width:"175px",height:"40px", mr:"20px"}}  onClick={handleAssignClick}>
                            {assignButtonText}
                        </Button>
                      <Button disabled={isDisabledSolve} variant={solvedButtonVariant} color="primary" sx={{width:"175px",height:"40px",mr:"20px"}}  onClick={handleSolvedClick}>
                            {solvedButtonText}
                        </Button>
                       <Button disabled={isDisabledDelete} variant="contained" color="primary" sx={{width:"40px",height:"40px"}}  onClick={()=>setOpenDeleteDialog(true)}>
                            <Delete sx={{color:"white"}}/>
                        </Button>
                    </Box>


                </Box>

                <Typography sx={{mb:2,ml:"3px"}} variant="body1" color="text.secondary" fontWeight="bold"  >
                    {issue.type}
                </Typography>
            </Box>

            <Box mb="20px" sx={{display: 'flex', flexDirection:'row', justifyContent:'space-between', width:"97%"}}>

                {/*bgcolor="#c0cedb"*/}
                <Box p="30px" pb="15px"  borderRadius={2} width="100%" mr="30px" sx={{display: 'flex', flexDirection:'column', width:"97%", background:'linear-gradient(135deg, #96b9d0, #bfd4db)'}}> {/**/}
                    <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'space-between'}}>
                        <Typography sx={{mb:3}} variant="h5" color="#192841" fontWeight="bold"  >
                        Details
                        </Typography>
                        <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'right'}}>
                            <FiberManualRecord sx={{color: statusDotColor}}/>
                            <Typography sx={{mb:1}} variant="body1" color="#192841" fontWeight="bold" >
                               {issue.status}
                            </Typography>
                        </Box>
                    </Box>

                    {involvement.username!==null &&
                        <Typography sx={{mb:1}} variant="body1" color="#192841"  >
                            <b>Assigned by:</b> {involvement.username}
                        </Typography>
                    }

                    <Typography sx={{mb:1}} variant="body1" color="#192841"  >
                        <b>Submitted by:</b> {issue.username}
                    </Typography>


                    <Typography sx={{mb:1}} variant="body1" color="#192841"  >
                        {issue.description}
                    </Typography>


                    <Box marginTop="auto" sx={{display: 'flex', flexDirection:'column', justifyContent:'bottom'}}>
                        <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'left'}}>
                            <LocationOn sx={{color:"#192841", mr:1}}/>
                            <Typography sx={{mb:1}} variant="body1" color="#192841"  >
                                {issue.location}
                            </Typography>
                        </Box>
                        <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'left'}}>
                            <Schedule sx={{color:"#192841",mr:1}}/>
                            <Typography sx={{mb:1}} variant="body1" color="#192841"  >
                                {issue.dateTime}
                            </Typography>
                        </Box>
                    </Box>
                </Box>
                <img src={issue.image} style={{maxWidth:"450px", borderRadius:5}} />

            </Box>

            <Box  mb="30px" sx={{display: 'flex', flexDirection:'row', justifyContent:'space-between', width:"97%"}}>
                <Box width="100%" mr="30px">
                    <Typography sx={{mb:1, ml:"3px"}} variant="h6" color="#192841"   >
                        Location
                    </Typography>
                    <Map coordinates={coordinates}/>
                </Box>
                <Box p="30px" pb="15px"  borderRadius={2} mt="37px" sx={{display: 'flex', flexDirection:'column', width:"655px",background:'linear-gradient(135deg, #bfd4db,#96b9d0 )'}}>
                    <Typography sx={{mb:2, ml:"3px"}} variant="h5" color="#192841" fontWeight="bold"  >
                        Share
                    </Typography>

                    <Box  mb="20px" sx={{display: 'flex', flexDirection:'row', justifyContent:"space-between"}}>
                        <FormControl  sx={{ width: 250 }} size="small">
                            <InputLabel>Reassign to</InputLabel>
                            <Select
                                id="reassign-checkbox"
                                small="true"
                                disabled={allInstitutions.length===0 || isDisabledReassignField}
                                value={reassignInstitution}
                                onChange={(e)=>setReassignInstitution(e.target.value)}
                                input={<OutlinedInput label="Institutions" />}
                            >
                                {allInstitutions.map((institution) => (
                                    <MenuItem key={institution.name} value={institution.name}>{institution.name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Button disabled={isDisabledReassign} variant="contained" color="primary" sx={{width:"120px",height:"39px"}}  onClick={handleReassignClick}>
                            Reassign
                        </Button>
                    </Box>

                    <Box mb="20px" sx={{display: 'flex', flexDirection:'row', justifyContent:"space-between"}}>
                        <FormControl  sx={{ width: 250 }} size="small">
                            <InputLabel>Share with</InputLabel>
                            <Select
                                id="share-checkbox"
                                multiple
                                small="true"
                                disabled={allInstitutions.length===0 || isDisabledShareField}
                                value={selectedShareInstitutions}
                                onChange={handleShareChange}
                                input={<OutlinedInput label="Institutions" />}
                                renderValue={(selected) => selected.join(', ')}
                            >
                                {allInstitutions.map((institution) => (
                                    <MenuItem key={institution.name} value={institution.name}>
                                        <Checkbox checked={selectedShareInstitutions.indexOf(institution.name) > -1} />
                                        <ListItemText primary={institution.name} />
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Button disabled={isDisabledShare} variant="contained" color="primary" sx={{width:"120px",height:"39px"}}  onClick={handleShareClick}>
                            Share
                        </Button>
                    </Box>

                    <Typography sx={{mb:2, ml:"3px"}} variant="h5" color="#192841" fontWeight="bold"  >
                        Involved Institutions
                    </Typography>

                    <Box
                         sx={{
                             "& .MuiDataGrid-root": {
                                 border: "1px solid #aaaaaa !important",
                             },
                             "& .MuiDataGrid-cell": {
                                 borderBottom: "1px solid #aaaaaa !important",

                             },
                             "& .MuiDataGrid-columnHeaders": {
                                 borderBottom: "1px solid #aaaaaa !important",
                             },
                             "& .MuiDataGrid-footerContainer": {
                                 borderTop: "none !important",
                                 // backgroundColor: "#cccccc",
                             },
                         }}
                    >
                        <DataGrid rows={involvedInstitutions}
                              columns={columns}
                              autoHeight
                                  hideFooterSelectedRowCount={true}

                        />
                    </Box>


                </Box>
            </Box>

            <Dialog open={openReassignDialog} >
                <DialogTitle>Reassign Issue?</DialogTitle>
                <DialogContent>
                  Are you sure you want to assign this issue to {reassignInstitution}? Once submitted, you won't be able to see it on your page anymore.
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={cancelReassign}>Cancel</Button>
                    <Button variant="contained" onClick={submitReassign}>Reassign</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openShareDialog} >
                <DialogTitle>Share Issue?</DialogTitle>
                <DialogContent>
                    Are you sure you want to share this issue with {selectedShareInstitutions.join(', ')}? Once submitted, the issue will also appear on their page and will only be solved once all parties mark it as solved.
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={cancelShare}>Cancel</Button>
                    <Button variant="contained" onClick={submitShare}>Share</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openSuccessDialog} >
                <DialogTitle>{successDialogTitle}</DialogTitle>
                <DialogContent>
                    {successDialogDescription}
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={closeSuccessDialog}>Close</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openDeleteDialog} >
                <DialogTitle>Delete issue?</DialogTitle>
                <DialogContent>
                    Are you sure you want to permanently delete this issue? In case you shared it with other institutions, it will also be deleted from their page.
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={()=>setOpenDeleteDialog(false)}>Cancel</Button>
                    <Button variant="contained" onClick={deleteIssue}>Delete</Button>
                </DialogActions>
            </Dialog>



            <Box mb="20px" p="31px" pb="15px" pt={"15px"} sx={{height:"500px", borderRadius:"5px", width:"92%",background:'linear-gradient(135deg, #bfd4db,#96b9d0 )', display:"flex",flexDirection:'column'}}>
                <Typography sx={{mb:1}} variant="h5" color="#192841" fontWeight="bold"    >
                    Chat with user and involved institutions
                </Typography>
                <Box  ref={chatRef} sx={{borderStyle:"solid",borderWidth:"1px",overflow:"auto",borderColor:"#888888", height:"100%",mb:"15px",borderRadius:"5px"}}>
                    {messages.map((message)=>
                    {
                        if(message.sourceName===institution.name){
                            return (
                                <Box mb="5px" sx={{display:"flex",justifyContent:"right"}}>
                                    <Box  width="70%" mr="10px">
                                        <Typography  variant="body1" color="#192841" textAlign="right" fontSize="14px" fontWeight="bold" key={message.position} >
                                            {message.sourceName}
                                        </Typography>
                                        <Box sx={{display:"flex",justifyContent:"right"}}>
                                            <Box  maxWidth="fit-content" borderRadius="10px" bgcolor="#1976d2" p="10px">
                                                <Typography  variant="body1" color="#ffffff"  key={message.position} >
                                                    {message.message}
                                                </Typography>
                                            </Box>
                                        </Box>


                                        <Typography  variant="body1" color="#192841" textAlign="right" fontSize="12px" key={message.position} >
                                            {message.timestamp}
                                        </Typography>
                                    </Box>

                                </Box>)


                        }else{
                            return (
                                <Box mb="5px" width="70%" ml="10px">
                                    <Typography  variant="body1" color="#192841"  key={message.position} fontSize="14px" fontWeight="bold" >
                                        {message.sourceName}
                                    </Typography>
                                    <Box maxWidth="fit-content" borderRadius="10px" bgcolor="#192841" p="10px">
                                        <Typography  variant="body1" color="#ffffff"  key={message.position} >
                                            {message.message}
                                        </Typography>
                                    </Box>

                                    <Typography  variant="body1" color="#192841" fontSize="12px" key={message.position} >
                                        {message.timestamp}
                                    </Typography>
                                </Box>)

                        }


                    })}

                </Box>
                <Box marginTop="auto" sx={{display: 'flex', flexDirection:'row', justifyContent:"space-between", alignItems:"center"}}>
                    <TextField disabled={isDisabledChat}
                        margin="dense"
                        id="message"
                        label="Type a message..."
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                    />
                    <Button disabled={isDisabledSend} variant="contained" sx={{height:"43px", width:"100px",ml:"20px", mt:"2px"}}  onClick={sendMessage}>
                        Send
                    </Button>

                </Box>

            </Box>

            <Box>
                    <SockJsClient url={'http://localhost:8081/ws'}
                                  topics={topics}
                                  onMessage={(message) => { showNewMessage(message) }}
                                  onConnect={()=>onConnect()}
                                  onDisconnect={()=>onDisconnect()}
                    />
            </Box>

        </Box>
    )
}
export default Issue;
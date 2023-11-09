import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {useParams} from "react-router";
import AppNavbar from "../navbar/AppNavbar";
import {
    Button,
    Card,
    CardBody,
    CardSubtitle,
    CardText,
    CardTitle,
    Container,
    Form,
    FormGroup,
    Input,
    Label
} from "reactstrap";
import {SuccessMessage} from "../orders/SuccessMessage";

function UserFound({ found, user, orderId, token, success, setSuccess }) {

    function share() {
        fetch("/orders/" + orderId, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({
                user: user
            })
        }).then(response => {
            if(response.ok) setSuccess(true);
        });
    }

    if (found) return <Container style={{paddingTop: 50, paddingBottom: 50}}>
        <Card style={{width: '18rem'}}>
            <CardBody>
                <CardTitle tag="h5">Usuario</CardTitle>
                <CardSubtitle className="mb-2 text-muted" tag="h6">
                    Información de usuario
                </CardSubtitle>
                <CardText>
                    {"Nombre de usuario: " + user.username}
                    <br/>
                    {"Email: " + user.email}
                </CardText>
                <Button color="success" onClick={() => share()}>
                    Compartir
                </Button>
            </CardBody>
        </Card>
        <SuccessMessage success={success}></SuccessMessage>
    </Container>;

}

function UserNotFound({ failure }) {
    if (failure) return <p style={{color: "red"}}> No se encontró al usuario </p>
}

export default function UserFind() {
    const [user, setUser] = useState({
        id: '',
        username: '',
        email: ''
    });
    const navigate = useNavigate();
    const { id } = useParams();
    const [found, setFound] = useState(false);
    const [success, setSuccess] = useState(false);
    const [failure, setFailure] = useState(false);
    let token = localStorage.getItem('token');

    function handleSubmit(e) {
        e.preventDefault();
        fetch("/users?username="+user.username+"&email="+user.email, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }).then(response => {
            if (!response.ok) throw new Error("User not found");
            return response.json();
        }).then(data => {
            setUser(data);
            setFound(true);
        }).catch((e) => {
            setFailure(true);
            console.log(e);
        });
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Buscar usuario</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Nombre de usuario</Label>
                    <Input type="text" id="username" defaultValue={user.username} autoComplete="username"
                           onChange={e => {
                               setUser({...user, username: e.target.value});
                               setFound(false);
                               setSuccess(false);
                           }}/>
                </FormGroup>
                <FormGroup>
                    <Label for="email">Email</Label>
                    <Input type="email" id="email" defaultValue={user.email} autoComplete="email"
                           onChange={e => {
                               setUser({...user, email: e.target.value});
                               setFound(false);
                               setSuccess(false);
                           }}/>
                </FormGroup>
                <UserNotFound failure={ failure }></UserNotFound>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Buscar</Button>{' '}
                    <Button color="secondary" onClick={() => navigate(-1)}>Volver</Button>
                </FormGroup>
            </Form>
        </Container>
        <UserFound found={found} user={user} orderId={id} token={token} success={success} setSuccess={setSuccess}/>
    </div>;
}
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
    FormFeedback,
    FormGroup,
    Input,
    Label
} from "reactstrap";
import {useAuth} from "../AuthContext";

export default function UserFind() {
    const [user, setUser] = useState({
        id: '',
        username: '',
        email: ''
    });
    const navigate = useNavigate();
    const {id} = useParams();
    const [found, setFound] = useState(false);
    const [notFound, setNotFound] = useState(false);
    const [success, setSuccess] = useState(false);
    const [failure, setFailure] = useState(false);
    let {token} = useAuth();

    function handleSubmit(e) {
        e.preventDefault();
        fetch("/users?username=" + user.username, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }).then(response => {
            if (!response.ok) {
                return response.text().then(body => {
                    throw new Error(body);
                });
            }
            return response.json();
        }).then(data => {
            setUser(data);
            setFound(true);
        }).catch((e) => {
            setNotFound(true);
            console.log(e);
        });
    }

    function handleChange(e) {
        setUser({...user, username: e.target.value});
        setFound(false);
        setNotFound(false);
        setSuccess(false);
        setFailure(false);
    }

    function share() {
        fetch("/orders/" + id, {
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
            if (!response.ok) {
                if (response.status === 401) {
                    alert("Pedido cerrado");
                    navigate("/orders");
                }
                else if (response.status === 409) {
                    setFailure(true);
                }
                return response.text().then(body => {
                    throw new Error(body)
                });
            }
            setSuccess(true);
        }).catch(console.log);
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Buscar usuario</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Nombre de usuario</Label>
                    <Input type="text" id="username" defaultValue={user.username} autoComplete="username"
                           onChange={handleChange} invalid={notFound}/>
                    <FormFeedback valid={!notFound}>No se encontró al usuario</FormFeedback>
                </FormGroup>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Buscar</Button>{' '}
                    <Button color="secondary" onClick={() => navigate("/orders/" + id)}>
                        Volver
                    </Button>
                </FormGroup>
            </Form>
        </Container>
        {found &&
            <Container style={{paddingTop: 50, paddingBottom: 50}}>
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
                <div style={{paddingTop: 30}}>
                    {success && <p style={{color: "green"}}> ¡Guardado! </p>}
                    {failure && <p style={{color: "red"}}> No se puede compartir el pedido con su dueño </p>}
                </div>
            </Container>
        }
    </div>
}
CREATE TABLE tb_user_session(
    id UUID PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    revoked_at TIMESTAMP,
    refresh_token_hash VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL REFERENCES tb_user (id) ON DELETE CASCADE
);
erDiagram
    
    USERS {
        bigint id PK
        string email
        string password_hash
        string nickname
        string status
        datetime created_at
        datetime updated_at
    }

    USER_ROLES {
        bigint id PK
        bigint user_id FK
        string role_type
        datetime created_at
    }

    INFLUENCERS {
        bigint id PK
        string name
        string display_name
        string category
        string profile_image_url
        string status
        datetime created_at
        datetime updated_at
    }

    PROJECTS {
        bigint id PK
        bigint influencer_id FK
        bigint created_by FK
        string title
        string description
        string cover_image_url
        string status
        datetime upload_start_at
        datetime upload_end_at
        datetime preview_start_at
        datetime preview_end_at
        datetime order_start_at
        datetime order_end_at
        datetime created_at
        datetime updated_at
    }

    PROJECT_OFFICIAL_IMAGES {
        bigint id PK
        bigint project_id FK
        string file_url
        string file_name
        string source_type
        datetime taken_at
        bigint uploaded_by FK
        datetime created_at
    }

    FAN_UPLOADS {
        bigint id PK
        bigint project_id FK
        bigint user_id FK
        string status
        string memo
        datetime created_at
        datetime updated_at
    }

    FAN_UPLOAD_FILES {
        bigint id PK
        bigint fan_upload_id FK
        bigint project_id FK
        bigint user_id FK
        string original_file_name
        string file_url
        string thumbnail_url
        string mime_type
        bigint file_size
        int width
        int height
        datetime uploaded_at
    }

    UPLOAD_AGREEMENTS {
        bigint id PK
        bigint fan_upload_file_id FK
        bigint user_id FK
        bigint project_id FK
        string agreed_terms_version
        boolean agreed_commercial_use
        boolean agreed_original_or_authorized
        boolean agreed_copyright_responsibility
        datetime agreed_at
    }

    IMAGE_ANALYSIS {
        bigint id PK
        bigint fan_upload_file_id FK
        decimal blur_score
        decimal brightness_score
        decimal noise_score
        boolean face_detected
        int face_count
        decimal resolution_score
        boolean watermark_detected
        boolean text_detected
        boolean inappropriate_flag
        string similarity_vector_path
        datetime analyzed_at
    }

    IMAGE_GROUPS {
        bigint id PK
        bigint project_id FK
        string group_key
        bigint representative_image_id FK
        decimal cluster_confidence
        datetime created_at
    }

    IMAGE_GROUP_ITEMS {
        bigint id PK
        bigint image_group_id FK
        bigint fan_upload_file_id FK
        decimal similarity_distance
        datetime created_at
    }

    CURATED_IMAGES {
        bigint id PK
        bigint project_id FK
        string source_type
        bigint source_image_id
        bigint image_group_id FK
        bigint selected_by FK
        string selected_reason
        int priority_order
        datetime created_at
    }

    PHOTOBOOKS {
        bigint id PK
        bigint project_id FK
        string title
        string subtitle
        string status
        int total_pages
        string cover_image_url
        datetime created_at
        datetime updated_at
    }

    PHOTOBOOK_PAGES {
        bigint id PK
        bigint photobook_id FK
        int page_number
        string page_type
        string layout_type
        string caption
        boolean is_preview_open
        datetime created_at
        datetime updated_at
    }

    PHOTOBOOK_PAGE_IMAGES {
        bigint id PK
        bigint photobook_page_id FK
        string image_source_type
        bigint image_source_id
        int position_order
        datetime created_at
    }

    ORDERS {
        bigint id PK
        bigint project_id FK
        bigint user_id FK
        string order_number
        string status
        decimal total_amount
        string recipient_name
        string recipient_phone
        string shipping_address
        datetime ordered_at
        datetime created_at
        datetime updated_at
    }

    ORDER_ITEMS {
        bigint id PK
        bigint order_id FK
        bigint photobook_id FK
        int quantity
        decimal unit_price
        datetime created_at
    }

    PRINT_BOOKS {
        bigint id PK
        bigint project_id FK
        bigint photobook_id FK
        string external_book_id
        string template_id
        string trim_size_id
        string status
        text raw_response_json
        datetime created_at
        datetime updated_at
    }

    PRINT_ORDERS {
        bigint id PK
        bigint order_id FK
        string external_order_id
        decimal estimate_price
        string status
        text raw_response_json
        datetime created_at
        datetime updated_at
    }

    WEBHOOK_EVENTS {
        bigint id PK
        string event_type
        string external_event_id
        text payload_json
        boolean processed
        datetime processed_at
        datetime created_at
    }

    USERS ||--o{ USER_ROLES : has
    USERS ||--o{ FAN_UPLOADS : creates
    USERS ||--o{ ORDERS : places
    USERS ||--o{ CURATED_IMAGES : selects
    USERS ||--o{ PROJECTS : creates

    INFLUENCERS ||--o{ PROJECTS : owns

    PROJECTS ||--o{ PROJECT_OFFICIAL_IMAGES : has
    PROJECTS ||--o{ FAN_UPLOADS : receives
    PROJECTS ||--o{ FAN_UPLOAD_FILES : contains
    PROJECTS ||--o{ IMAGE_GROUPS : has
    PROJECTS ||--o{ CURATED_IMAGES : produces
    PROJECTS ||--|| PHOTOBOOKS : generates
    PROJECTS ||--o{ ORDERS : receives
    PROJECTS ||--o{ PRINT_BOOKS : maps

    FAN_UPLOADS ||--o{ FAN_UPLOAD_FILES : contains
    FAN_UPLOAD_FILES ||--|| UPLOAD_AGREEMENTS : has
    FAN_UPLOAD_FILES ||--|| IMAGE_ANALYSIS : analyzed_as
    FAN_UPLOAD_FILES ||--o{ IMAGE_GROUP_ITEMS : grouped_in

    IMAGE_GROUPS ||--o{ IMAGE_GROUP_ITEMS : contains
    IMAGE_GROUPS ||--o{ CURATED_IMAGES : referenced_by

    PHOTOBOOKS ||--o{ PHOTOBOOK_PAGES : has
    PHOTOBOOK_PAGES ||--o{ PHOTOBOOK_PAGE_IMAGES : contains

    ORDERS ||--o{ ORDER_ITEMS : has
    ORDERS ||--|| PRINT_ORDERS : maps
    PHOTOBOOKS ||--o{ ORDER_ITEMS : ordered_as
    PHOTOBOOKS ||--|| PRINT_BOOKS : printed_as